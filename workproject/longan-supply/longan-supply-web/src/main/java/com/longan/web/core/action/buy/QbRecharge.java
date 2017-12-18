package com.longan.web.core.action.buy;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.longan.biz.core.ItemService;
import com.longan.biz.dataobject.AcctInfo;
import com.longan.biz.dataobject.BizOrder;
import com.longan.biz.dataobject.Item;
import com.longan.biz.domain.Result;
import com.longan.biz.utils.Constants;
import com.longan.biz.utils.Utils;
import com.longan.client.remote.domain.web.BuyItem;
import com.longan.web.biz.PayPwdService;
import com.longan.web.biz.RechargeService;
import com.longan.web.biz.domain.LoginUserInfo;
import com.longan.web.biz.domain.PayToken;
import com.longan.web.common.controller.BaseController;
import com.longan.web.core.form.BasePayForm;
import com.longan.web.core.form.QbRechargeForm;

@Controller
public class QbRecharge extends BaseController {
	private final static String qbItemid = Utils.getProperty("qbItemId");
	@Resource
	private ItemService itemService;

	@Resource
	private PayPwdService payPwdService;

	@Resource
	private RechargeService rechargeService;

	@RequestMapping("buy/qbRecharge")
	public void qbRechargeIndex(HttpServletRequest request, Model model) {
		Result<Item> result = itemService.getItem(Integer.parseInt(qbItemid));
		if (result.isSuccess()) {
			Item item = result.getModule();
			if (item.getStatus() != Constants.Item.STATUS_UP) {
				model.addAttribute("itemInfo", "抱歉该商品，未上架");
			} else {
				LoginUserInfo loginUserInfo = super.getUserInfo(request);
				AcctInfo acctInfo = localCachedService.getAcctInfoNot4Trade(loginUserInfo
						.getUserInfo().getAcctId());
				if (acctInfo == null) {
					model.addAttribute("itemInfo", "抱歉,遇到一个问题,请联系客服人员");
				}
				Result<Integer> priceResult = itemService.getPriceByAcct(item, acctInfo);
				if (!priceResult.isSuccess()) {
					model.addAttribute("itemInfo", "抱歉,遇到一个问题,请联系客服人员");
				}
				Integer salesPrice = priceResult.getModule();
				model.addAttribute("salesPrice", salesPrice);
				model.addAttribute("item", item);
			}
		} else {
			model.addAttribute("itemInfo", "抱歉该商品，未上架");
		}
		hasSetPayPwd(request, model);
	}

	@RequestMapping("buy/qbRechargeConfirm")
	public void qqRechargeConfirm(@ModelAttribute("qbRechargeForm") QbRechargeForm qbRechargeForm,
			BindingResult bindingResult, HttpServletRequest request, Model model) {
		LoginUserInfo loginUserInfo = super.getUserInfo(request);

		if (super.isFormError(bindingResult, model, qbRechargeForm)) {
			return;
		}
		Result<Item> itemResult = itemService.getItem(Integer.parseInt(qbItemid));
		if (!itemResult.isSuccess() || itemResult.getModule() == null) {
			setErrorMsg(model, StringUtils.defaultIfEmpty(itemResult.getResultMsg(), "未找到该商品"));
			return;
		}
		Item item = itemResult.getModule();
		AcctInfo acctInfo = localCachedService.getAcctInfoNot4Trade(loginUserInfo.getUserInfo()
				.getAcctId());
		if (acctInfo == null) {
			setErrorMsg(model, "账户信息不存在");
			return;
		}
		Result<Integer> priceResult = itemService.getPriceByAcct(itemResult.getModule(), acctInfo);
		if (!priceResult.isSuccess()) {
			setErrorMsg(model, StringUtils.defaultIfEmpty(priceResult.getResultMsg(), "账户查询失败"));
			return;
		}
		Result<PayToken> tokenResult = payPwdService.getPayToken(item.getBizId(),
				loginUserInfo.getUserInfo());
		if (!tokenResult.isSuccess() || tokenResult.getModule() == null) {
			setErrorMsg(model,
					StringUtils.defaultIfEmpty(tokenResult.getResultMsg(), "payToken初始化失败"));
			return;
		}
		PayToken payToken = tokenResult.getModule();
		initForm(qbRechargeForm, payToken);
		model.addAttribute("qbRechargeForm", qbRechargeForm);

	}

	private final static String errorUrl = "buy/qbRechargeConfirm";

	@RequestMapping("buy/qbRechargePayment")
	public ModelAndView qbRechargePayment(
			@ModelAttribute("qbRechargeForm") QbRechargeForm qbRechargeForm,
			BindingResult bindingResult, HttpServletRequest request, Model model) {
		if (super.isFormError(bindingResult, model, qbRechargeForm)) {
			return new ModelAndView(errorUrl);
		}
		LoginUserInfo loginUserInfo = super.getUserInfo(request);

		Item item = localCachedService.getItem(Integer.parseInt(qbItemid));
		if (item == null) {
			model.addAttribute("errorMsg", "没有该商品");
			return new ModelAndView(errorUrl);
		}

		Result<PayToken> tokenResult = payPwdService.getPayToken(item.getBizId(),
				loginUserInfo.getUserInfo());
		if (!tokenResult.isSuccess() || tokenResult.getModule() == null) {
			model.addAttribute("errorMsg", "payToken获取失败");
			return new ModelAndView(errorUrl);
		}

		PayToken payToken = tokenResult.getModule();

		if (!csrfTokenManager.checkToken(request, loginUserInfo.getUserInfo().getId())) {
			model.addAttribute("errorMsg", "你的操作已超时，或重复提交");
			return new ModelAndView(errorUrl);
		}
		Result<Boolean> payPwdCheck = payPwdService.checkPayPwd(qbRechargeForm,
				loginUserInfo.getUserInfo(), payToken);
		if (!payPwdCheck.isSuccess()) {
			initForm(qbRechargeForm, payToken);
			model.addAttribute("errorMsg", payPwdCheck.getResultMsg());
			return new ModelAndView(errorUrl);
		}

		if (!payPwdCheck.getModule()) {
			initForm(qbRechargeForm, payToken);

			List<ObjectError> allErrors = new ArrayList<ObjectError>();
			allErrors.add(new FieldError("flowRechargePaymentForm", "payPwd", "支付密码错误"));
			model.addAttribute("errorList", allErrors);

			return new ModelAndView(errorUrl);
		}

		BuyItem buyItem = new BuyItem();
		buyItem.setUserId(loginUserInfo.getUserInfo().getId());
		buyItem.setUid(qbRechargeForm.getUid());
		buyItem.setItemId(Integer.parseInt(qbItemid));
		buyItem.setAmt(Integer.parseInt(qbRechargeForm.getAmount()));

		// 必须异步，等不起
		buyItem.setSupplyWay(Constants.BizOrder.DOWNSTREAM_SUPPLY_WAY_ASYNC);

		Result<BizOrder> buyResult = rechargeService.payment(buyItem);

		Map<String, Object> paramMap = new HashMap<String, Object>();
		if (!buyResult.isSuccess() || buyResult.getModule() == null) {
			if (Constants.ErrorCode.CODE_ERROR_BALANCE.equals(buyResult.getResultCode())) {
				initForm(qbRechargeForm, payToken);
				List<ObjectError> allErrors = new ArrayList<ObjectError>();
				allErrors.add(new FieldError("flowRechargePaymentForm", "payPwd",
						"余额不足，点击<a href='../funds/acctIn.htm' target='_blank'>这里</a>充值"));
				model.addAttribute("errorList", allErrors);
				return new ModelAndView(errorUrl);
			}

			if (buyResult.getResultMsg() != null) {
				try {
					paramMap.put("errorMsg", URLEncoder.encode(buyResult.getResultMsg(), "utf-8"));
				} catch (UnsupportedEncodingException e) {
				}
			}
			paramMap.put("type", PaymentResult.TYPE_NO_BIZORDER);
			return new ModelAndView(new RedirectView("paymentResult.htm"), paramMap);
		}

		paramMap.put("id", buyResult.getModule().getId());
		paramMap.put("type", PaymentResult.TYPE_DEFAULT);
		return new ModelAndView(new RedirectView("paymentResult.htm"), paramMap);
	}

	private void initForm(BasePayForm basePayForm, PayToken payToken) {
		basePayForm.setToken(payToken.getToken());
		basePayForm.setTs(payToken.getTs() + "");
	}

	@RequestMapping("buy/multiQbRecharge")
	public void multiQbRechargeIndex() {

	}
}
