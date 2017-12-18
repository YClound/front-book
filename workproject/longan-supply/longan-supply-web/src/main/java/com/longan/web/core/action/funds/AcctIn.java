package com.longan.web.core.action.funds;

import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.longan.biz.core.ChargeOrderService;
import com.longan.biz.dataobject.ChargeOrder;
import com.longan.biz.dataobject.UserInfo;
import com.longan.biz.domain.OperationVO;
import com.longan.biz.domain.Result;
import com.longan.biz.utils.BigDecimalUtils;
import com.longan.web.biz.domain.LoginUserInfo;
import com.longan.web.common.controller.BaseController;
import com.longan.web.core.form.AcctInForm;
import com.longan.web.utils.Constants;

@Controller
public class AcctIn extends BaseController {
	@Resource
	private ChargeOrderService chargeOrderService;

	@RequestMapping("funds/acctIn")
	void index() {

	}

	@RequestMapping("funds/acctInCommit")
	String commit(@ModelAttribute("acctInForm") AcctInForm acctInForm, BindingResult bindingResult,
			Model model, HttpServletRequest request) {
		final String returnUrl = "funds/acctIn";
		LoginUserInfo loginUserInfo = super.getUserInfo(request);
		// acct 判断
		UserInfo userInfo = loginUserInfo.getUserInfo();
		if (userInfo == null || userInfo.getAcctId() == null) {
			model.addAttribute("errorMsg", "账户信息不存在");
			return returnUrl;
		}

		if (!csrfTokenManager.checkToken(request, loginUserInfo.getUserInfo().getId())) {
			model.addAttribute("errorMsg", "你的操作已经超时，或重复提交");
			return returnUrl;
		}

		validator.validate(acctInForm, bindingResult);
		if (bindingResult.hasErrors()) {
			model.addAttribute("errorList", bindingResult.getAllErrors());
			return returnUrl;
		}

		OperationVO operationVO = new OperationVO();
		operationVO.setUserInfo(userInfo);

		ChargeOrder chargeOrder = new ChargeOrder();
		chargeOrder.setUserId(userInfo.getId());
		chargeOrder.setAcctId(userInfo.getAcctId());
		chargeOrder.setAmount(BigDecimalUtils.multLong(acctInForm.getAmount()));
		chargeOrder.setMemo(acctInForm.getBank()
				+ " "
				+ (StringUtils.isEmpty(acctInForm.getBankSerialNO()) ? ""
						: ("银行流水号(备注): " + acctInForm.getBankSerialNO())));

		Result<ChargeOrder> result = chargeOrderService.createChargeOrder(operationVO, chargeOrder);
		model.addAttribute("result", result);
		return "funds/acctInResult";
	}

	@ModelAttribute("bankList")
	public Map<String, String> bankList() {
		TreeMap<String, String> map = new TreeMap<String, String>();
		map.put(Constants.Bank.BANK_1, Constants.Bank.BANK_1_DESC);
		map.put(Constants.Bank.BANK_2, Constants.Bank.BANK_2_DESC);
//		map.put(Constants.Bank.BANK_3, Constants.Bank.BANK_3_DESC);
		// map.put(Constants.Bank.BANK_4, Constants.Bank.BANK_4_DESC);
		return map;
	}

}
