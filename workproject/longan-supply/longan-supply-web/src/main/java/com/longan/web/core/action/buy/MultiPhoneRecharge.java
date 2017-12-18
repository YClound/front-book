package com.longan.web.core.action.buy;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;

import com.longan.biz.core.AreaInfoService;
import com.longan.biz.core.ItemService;
import com.longan.biz.domain.Result;
import com.longan.biz.utils.Constants;
import com.longan.client.remote.domain.web.BuyFacePrice;
import com.longan.client.remote.service.SupplyForRemoteService;
import com.longan.web.biz.PayPwdService;
import com.longan.web.biz.RechargeService;
import com.longan.web.biz.domain.LoginUserInfo;
import com.longan.web.biz.domain.PayToken;
import com.longan.web.common.controller.BaseController;
import com.longan.web.core.form.BasePayForm;
import com.longan.web.core.form.MultiPhoneRechargeForm;

@Controller
public class MultiPhoneRecharge extends BaseController {

	@Resource
	private CommonsMultipartResolver multipartResolver;

	@Resource
	private RechargeService rechargeService;

	@Resource
	private AreaInfoService areaInfoService;

	@Resource
	private ItemService itemService;

	@Resource
	private PayPwdService payPwdService;

	@Resource
	private SupplyForRemoteService supplyForRemoteService;

	@RequestMapping("buy/multiPhoneRecharge")
	public void rechargeIndex(HttpServletRequest request, Model model) {
		hasSetPayPwd(request, model);
		LoginUserInfo loginUserInfo = super.getUserInfo(request);

		Result<PayToken> tokenResult = payPwdService.getPayToken(Constants.BizInfo.CODE_PHONE_MOBILE, loginUserInfo.getUserInfo());
		if (!tokenResult.isSuccess() || tokenResult.getModule() == null) {
			setErrorMsg(model, StringUtils.defaultIfEmpty(tokenResult.getResultMsg(), "payToken初始化失败"));
			return;
		}

		PayToken payToken = tokenResult.getModule();
		MultiPhoneRechargeForm multiPhoneRechargeForm = new MultiPhoneRechargeForm();
		multiPhoneRechargeForm.setFacePrice("other");
		initForm(multiPhoneRechargeForm, payToken);
		model.addAttribute("multiPhoneRechargeForm", multiPhoneRechargeForm);
	}

	private final static String returnUrl = "buy/multiPhoneRecharge";

	@RequestMapping("buy/multiPhoneRechargePayment")
	public ModelAndView rechargeConfirm(@ModelAttribute("multiPhoneRechargeForm") MultiPhoneRechargeForm multiPhoneRechargeForm, BindingResult bindingResult, HttpServletRequest request, Model model) {

		if (super.isFormError(bindingResult, model, multiPhoneRechargeForm)) {
			return new ModelAndView(returnUrl);
		}
		LoginUserInfo loginUserInfo = super.getUserInfo(request);

		Result<PayToken> tokenResult = payPwdService.getPayToken(Constants.BizInfo.CODE_PHONE_MOBILE, loginUserInfo.getUserInfo());
		if (!tokenResult.isSuccess() || tokenResult.getModule() == null) {
			model.addAttribute("errorMsg", "payToken获取失败");
			return new ModelAndView(returnUrl);
		}

		PayToken payToken = tokenResult.getModule();

		if (!csrfTokenManager.checkToken(request, loginUserInfo.getUserInfo().getId())) {
			model.addAttribute("errorMsg", "你的操作已超时，或重复提交");
			return new ModelAndView(returnUrl);
		}

		Result<Boolean> payPwdCheck = payPwdService.checkPayPwd(multiPhoneRechargeForm, loginUserInfo.getUserInfo(), payToken);
		if (!payPwdCheck.isSuccess()) {
			initForm(multiPhoneRechargeForm, payToken);
			model.addAttribute("errorMsg", payPwdCheck.getResultMsg());
			return new ModelAndView(returnUrl);
		}

		initForm(multiPhoneRechargeForm, payToken);
		if (!payPwdCheck.getModule()) {
			List<ObjectError> allErrors = new ArrayList<ObjectError>();
			allErrors.add(new FieldError("multiFlowRechargeForm", "payPwd", "支付密码错误"));
			model.addAttribute("errorList", allErrors);

			return new ModelAndView(returnUrl);
		}

		File file = this.uploadFile(request);
		if (file == null) {
			this.setFormError("uploadFile", "请上传批充文件", model);
			return new ModelAndView(returnUrl);
		}
		// 解析文件
		List<BuyFacePrice> list = null;
		try {
			list = this.parseExcel(file, loginUserInfo, multiPhoneRechargeForm.getFacePrice());
		} catch (Exception e) {
			logger.error("excel文件解析失败", e);
			this.setFormError("uploadFile", "excel文件解析失败", model);
			return new ModelAndView(returnUrl);
		}

		if (list == null) {
			this.setFormError("uploadFile", "必须是以xls或者xlsx的excel文件", model);
			return new ModelAndView(returnUrl);
		}

		logger.warn("--->multiPhoneRechargeForm=" + multiPhoneRechargeForm);

		Result<Integer> bantchBuyPhoneByFacePriceResult = new Result<Integer>();
		try {
			bantchBuyPhoneByFacePriceResult = supplyForRemoteService.bantchBuyPhoneByFacePrice(list);
		} catch (Exception e) {
			logger.error("buy error", e);
			if (e instanceof SocketTimeoutException) {
				bantchBuyPhoneByFacePriceResult.setResultMsg("交易处理中，结果未确认");
			} else {
				bantchBuyPhoneByFacePriceResult.setResultMsg("网络连接失败");
			}
		}

		model.addAttribute("type", 2);
		model.addAttribute("result", bantchBuyPhoneByFacePriceResult);
		model.addAttribute("count", list.size());
		return new ModelAndView("buy/multiPaymentResult");
	}

	@ModelAttribute("phoneMap")
	public Map<String, String> flowMap() {
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("other", "其他面值 ");
		map.put("10000", "10元 ");
		map.put("20000", "20元");
		map.put("30000", "30元 ");
		map.put("100000", "100元");
		return map;
	}

	private void setFormError(String name, String msg, Model model) {
		List<ObjectError> allErrors = new ArrayList<ObjectError>();
		allErrors.add(new FieldError("multiFlowRechargeForm", name, msg));
		model.addAttribute("errorList", allErrors);
	}

	private void initForm(BasePayForm basePayForm, PayToken payToken) {
		basePayForm.setToken(payToken.getToken());
		basePayForm.setTs(payToken.getTs() + "");
	}

	private List<BuyFacePrice> parseExcel(File file, LoginUserInfo loginUserInfo, String facePrice) throws IOException {
		List<BuyFacePrice> result = new ArrayList<BuyFacePrice>();
		int rowSize = 0;
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
		try {
			Workbook wb = null;
			if (file.getPath().endsWith("xls") || file.getPath().endsWith("XLS")) {
				wb = new HSSFWorkbook(in);
			} else if (file.getPath().endsWith("xlsx") || file.getPath().endsWith("XLSX")) {
				wb = new XSSFWorkbook(in);
			} else {
				return null;
			}
			Cell cell = null;
			int ignoreRows = 1;

			for (int sheetIndex = 0; sheetIndex < wb.getNumberOfSheets(); sheetIndex++) {
				Sheet st = wb.getSheetAt(sheetIndex);
				// 第一行为标题，不取
				for (int rowIndex = ignoreRows; rowIndex <= st.getLastRowNum(); rowIndex++) {
					Row row = st.getRow(rowIndex);
					if (row == null) {
						continue;
					}
					int tempRowSize = row.getLastCellNum() + 1;
					if (tempRowSize > rowSize) {
						rowSize = tempRowSize;
					}
					BuyFacePrice buyFacePrice = new BuyFacePrice();
					result.add(buyFacePrice);

					buyFacePrice.setUserId(loginUserInfo.getUserInfo().getId());

					int columnSize = 1;
					if ("other".equals(facePrice)) {
						columnSize = 2;
					} else {
						buyFacePrice.setItemFacePrice(facePrice);
					}

					for (int columnIndex = 0; columnIndex < columnSize; columnIndex++) {
						String value = "";
						cell = row.getCell(columnIndex);
						if (cell != null) {
							// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
							switch (cell.getCellType()) {
							case HSSFCell.CELL_TYPE_STRING:
								value = cell.getStringCellValue();
								break;
							case HSSFCell.CELL_TYPE_NUMERIC:
								value = new DecimalFormat("0").format(cell.getNumericCellValue());
								break;
							case HSSFCell.CELL_TYPE_FORMULA:
								// 导入时如果为公式生成的数据则无值
								if (!cell.getStringCellValue().equals("")) {
									value = cell.getStringCellValue();
								} else {
									value = cell.getNumericCellValue() + "";
								}
								break;
							case HSSFCell.CELL_TYPE_BLANK:
								break;
							case HSSFCell.CELL_TYPE_ERROR:
								value = "";
								break;
							default:
								value = "";
							}
						}
						if (StringUtils.isBlank(value)) {
							continue;
						}
						if (columnIndex == 0) {
							buyFacePrice.setUid(value);
						} else if (columnIndex == 1) {
							if (StringUtils.isNumeric(value)) {
								buyFacePrice.setItemFacePrice(Integer.parseInt(value) * 1000 + "");
							}
						}
					}
				}

			}

		} finally {
			in.close();
		}

		return result;
	}

	public static void main(String[] args) throws IOException {
		// File file = new File("d:/1.xlsx");
		// List<BuyFacePrice> result = parseExcel(file);
		// for (BuyFacePrice buyFacePrice : result) {
		// System.out.println(buyFacePrice.getUid() + "----" +
		// buyFacePrice.getItemFacePrice());
		// }
	}

	private File uploadFile(HttpServletRequest request) {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		File file = null;

		if (multipartResolver.isMultipart(multipartRequest)) {// 判断request是否有文件上传
			// srcfname是指文件上传标签的name=值
			MultiValueMap<String, MultipartFile> multfiles = multipartRequest.getMultiFileMap();
			for (String srcfname : multfiles.keySet()) {
				MultipartFile mfile = multfiles.getFirst(srcfname);

				try {
					file = new File(com.longan.web.utils.Constants.UPLOAD_PATH + System.nanoTime() + "_" + mfile.getOriginalFilename());
					if (mfile.isEmpty()) {
						return null;
					}
					mfile.transferTo(file);
				} catch (IllegalStateException e) {
					logger.error("uploadFile error ", e);
					return null;
				} catch (IOException e) {
					logger.error("uploadFile error ", e);
					return null;
				}
			}
		} else {
			logger.error("uploadFile error file is null");
			return null;
		}
		return file;
	}

}
