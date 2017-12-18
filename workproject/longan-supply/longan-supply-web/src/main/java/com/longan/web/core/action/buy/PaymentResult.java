package com.longan.web.core.action.buy;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.longan.biz.core.BizOrderService;
import com.longan.biz.dataobject.BizOrder;
import com.longan.biz.domain.Result;
import com.longan.web.biz.domain.LoginUserInfo;
import com.longan.web.common.controller.BaseController;

@Controller
public class PaymentResult extends BaseController {
	@Resource
	private BizOrderService bizOrderService;

	public static final int TYPE_DEFAULT = 0;
	public static final int TYPE_NO_BIZORDER = 1;

	@RequestMapping("buy/paymentResult")
	public void result(@RequestParam(value = "type", defaultValue = "0") Integer type, Model model,
			HttpServletRequest request) {
		LoginUserInfo loginUserInfo = super.getUserInfo(request);

		if (type == TYPE_DEFAULT) {
			String bizOrderIdStr = request.getParameter("id");
			if (!StringUtils.isNumeric(bizOrderIdStr)) {
				super.setErrorMsg(model, "没有该订单信息");
				return;
			}
			Long bizOrderId = Long.parseLong(bizOrderIdStr);
			Result<BizOrder> bizOrderResult = bizOrderService.getBizOrderById(bizOrderId);
			if (!bizOrderResult.isSuccess() || bizOrderResult.getModule() == null) {
				super.setErrorMsg(model,
						StringUtils.defaultIfEmpty(bizOrderResult.getResultMsg(), "没有该订单信息"));
				return;
			}

			BizOrder bizOrder = bizOrderResult.getModule();
			if (!loginUserInfo.getUserInfo().getId().equals(bizOrder.getUserId())) {
				super.setErrorMsg(model, "没有该订单信息");
			}

			model.addAttribute("bizOrder", bizOrder);
		} else if (type == TYPE_NO_BIZORDER) {
			String errorMsg = request.getParameter("errorMsg");
			if (errorMsg != null) {
				try {
					errorMsg = URLDecoder.decode(errorMsg, "utf-8");
				} catch (UnsupportedEncodingException e) {
					logger.error("", e);
				}
			}
			super.setErrorMsg(model, errorMsg);
		}
	}
}
