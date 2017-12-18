package com.longan.mng.action.biz;

import java.lang.reflect.Field;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.longan.biz.core.BizOrderService;
import com.longan.biz.core.SupplyOrderService;
import com.longan.biz.dataobject.BizOrder;
import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.dataobject.SupplyOrderQuery;
import com.longan.biz.dataobject.UserInfo;
import com.longan.biz.domain.Result;
import com.longan.biz.utils.Constants;
import com.longan.biz.utils.DateTool;
import com.longan.client.remote.service.CallBackService;
import com.longan.mng.action.common.BaseController;

@Controller
public class ManualSupplyOrderDeal extends BaseController {

	private static final String returnUrl = "redirect:queryManualSupplyOrder.do";

	private static final String errorUrl = "biz/queryManualSupplyOrder";

	@Resource
	private BizOrderService bizOrderService;

	@Resource
	private SupplyOrderService supplyOrderService;

	@Resource
	private CallBackService callBackService;

	@RequestMapping("biz/supplyOrderLock")
	public String lockRequest(@RequestParam("supplyOrderId") Long supplyOrderId,
			@ModelAttribute("supplyOrderQuery") SupplyOrderQuery supplyOrderQuery,
			HttpSession session, Model model) {
		UserInfo userInfo = super.getUserInfo(session);
		logger.warn(userInfo.getUserName() + "执行供货单锁定操作 供货单号 : " + supplyOrderId);

		Result<SupplyOrder> queryReuslt = supplyOrderService.getSupplyOrderById(supplyOrderId);
		if (!queryReuslt.isSuccess() || queryReuslt.getModule() == null) {
			alertError(model, "没有该供货单");
			return errorUrl;
		}

		SupplyOrder supplyOrder = queryReuslt.getModule();
		supplyOrder.setLockOperId(userInfo.getId());

		Result<Boolean> updateResult = supplyOrderService.manualLockSupplyOrder(supplyOrder);
		if (updateResult.isSuccess() && updateResult.getModule()) {
			supplyOrderQuery.setId(supplyOrderId);
			supplyOrderQuery.setSupplyStatus(Constants.SupplyOrder.STATUS_LOCK);
			String url = returnUrl + formToParam(supplyOrderQuery);
			alertSuccessNoneBack(model);
			return url;
		} else {
			alertError(model, updateResult.getResultMsg());
		}
		return errorUrl;
	}

	@RequestMapping("biz/supplyOrderUnLock")
	public String unlockRequest(@RequestParam("supplyOrderId") Long supplyOrderId,
			@ModelAttribute("supplyOrderQuery") SupplyOrderQuery supplyOrderQuery,
			HttpSession session, Model model) {
		UserInfo userInfo = super.getUserInfo(session);
		logger.warn(userInfo.getUserName() + "执行供货单解锁操作 供货单号 : " + supplyOrderId);

		Result<SupplyOrder> queryReuslt = supplyOrderService.getSupplyOrderById(supplyOrderId);
		if (!queryReuslt.isSuccess() || queryReuslt.getModule() == null) {
			alertError(model, "没有该供货单");
			return errorUrl;
		}

		SupplyOrder supplyOrder = queryReuslt.getModule();
		supplyOrder.setDealOperId(userInfo.getId());

		Result<Boolean> updateResult = supplyOrderService.manualUnLockSupplyOrder(supplyOrder);
		if (updateResult.isSuccess() && updateResult.getModule()) {
			supplyOrderQuery.setSupplyStatus(Constants.SupplyOrder.STATUS_CHARGING);
			String url = returnUrl + formToParam(supplyOrderQuery);
			alertSuccessNoneBack(model);
			return url;
		} else {
			alertError(model, updateResult.getResultMsg());
		}
		return errorUrl;
	}

	@RequestMapping("biz/supplyOrderCancel")
	public String cancelRequest(@RequestParam("supplyOrderId") Long supplyOrderId,
			@ModelAttribute("supplyOrderQuery") SupplyOrderQuery supplyOrderQuery,
			HttpSession session, Model model) {
		UserInfo userInfo = super.getUserInfo(session);
		logger.warn(userInfo.getUserName() + "执行供货单取消操作 供货单号 : " + supplyOrderId);

		Result<SupplyOrder> queryReuslt = supplyOrderService.getSupplyOrderById(supplyOrderId);
		if (!queryReuslt.isSuccess() || queryReuslt.getModule() == null) {
			alertError(model, "没有该供货单");
			return errorUrl;
		}

		SupplyOrder supplyOrder = queryReuslt.getModule();
		supplyOrder.setDealOperId(userInfo.getId());

		Result<Boolean> updateResult = supplyOrderService.manualCancelSupplyOrder(supplyOrder);
		if (!updateResult.isSuccess()) {
			alertError(model, updateResult.getResultMsg());
			return errorUrl;
		}

		Result<BizOrder> bizOrderQueryReuslt = bizOrderService.getBizOrderById(supplyOrder
				.getBizOrderId());

		BizOrder bizOrder = null;
		if (!bizOrderQueryReuslt.isSuccess()) {
			alertError(model, bizOrderQueryReuslt.getResultMsg());
			return errorUrl;
		}

		if (bizOrderQueryReuslt.getModule() == null) {
			alertError(model, "该供货单，查询不到订单信息");
			return errorUrl;
		}

		bizOrder = bizOrderQueryReuslt.getModule();

		Result<Boolean> bizOrderDealResult = bizOrderService.cancelBizOrder(bizOrder);

		if (!bizOrderDealResult.isSuccess()) {
			alertError(model, bizOrderDealResult.getResultMsg());
			return errorUrl;
		}

		// 通知下游
		try {
			bizOrder.setStatus(Constants.BizOrder.STATUS_FAILED);
			Result<Boolean> callBackResult = callBackService.callBackAsync(bizOrder);
			if (!callBackResult.isSuccess() || !callBackResult.getModule()) {
				logger.error("cancelBizOrder callback failed bizOrderId : " + bizOrder.getId(),
						callBackResult.getResultMsg());
			}
		} catch (Exception e) {
			logger.error("cancelBizOrder callback failed bizOrderId : " + bizOrder.getId(), e);
		}

		supplyOrderQuery.setId(supplyOrderId);
		supplyOrderQuery.setSupplyStatus(Constants.SupplyOrder.STATUS_FAILED);
		String url = returnUrl + formToParam(supplyOrderQuery);
		alertSuccessNoneBack(model);
		return url;
	}

	@RequestMapping("biz/supplyOrderConfirm")
	public String confirmRequest(@RequestParam("supplyOrderId") Long supplyOrderId,
			@ModelAttribute("supplyOrderQuery") SupplyOrderQuery supplyOrderQuery,
			HttpSession session, Model model) {
		UserInfo userInfo = super.getUserInfo(session);
		logger.warn(userInfo.getUserName() + "执行供货单确认操作 供货单号 : " + supplyOrderId);
		Result<SupplyOrder> queryReuslt = supplyOrderService.getSupplyOrderById(supplyOrderId);
		if (!queryReuslt.isSuccess() || queryReuslt.getModule() == null) {
			alertError(model, "没有该供货单");
			return errorUrl;
		}

		SupplyOrder supplyOrder = queryReuslt.getModule();
		supplyOrder.setDealOperId(userInfo.getId());

		Result<Boolean> updateResult = supplyOrderService.manualConfirmSupplyOrder(supplyOrder);

		if (!updateResult.isSuccess()) {
			alertError(model, updateResult.getResultMsg());
			return errorUrl;
		}

		Result<BizOrder> bizOrderQueryReuslt = bizOrderService.getBizOrderById(supplyOrder
				.getBizOrderId());

		BizOrder bizOrder = null;
		if (!bizOrderQueryReuslt.isSuccess()) {
			alertError(model, bizOrderQueryReuslt.getResultMsg());
			return errorUrl;
		}

		if (bizOrderQueryReuslt.getModule() == null) {
			alertError(model, "该供货单，查询不到订单信息");
			return errorUrl;
		}

		bizOrder = bizOrderQueryReuslt.getModule();

		Result<Boolean> bizOrderDealResult = bizOrderService.comfirmBizOrder(bizOrder);

		if (!bizOrderDealResult.isSuccess()) {
			alertError(model, bizOrderDealResult.getResultMsg());
			return errorUrl;
		}

		// 通知下游
		try {
			bizOrder.setStatus(Constants.BizOrder.STATUS_SUCCESS);
			Result<Boolean> callBackResult = callBackService.callBackAsync(bizOrder);
			if (!callBackResult.isSuccess() || !callBackResult.getModule()) {
				logger.error("comfirmBizOrder callback failed bizOrderId : " + bizOrder.getId(),
						callBackResult.getResultMsg());
			}
		} catch (Exception e) {
			logger.error("cancelBizOrder callback failed bizOrderId : " + bizOrder.getId(), e);
		}

		supplyOrderQuery.setId(supplyOrderId);
		supplyOrderQuery.setSupplyStatus(Constants.SupplyOrder.STATUS_SUCCESS);
		String url = returnUrl + formToParam(supplyOrderQuery);
		alertSuccessNoneBack(model);
		return url;
	}

	@RequestMapping("biz/supplyOrderUnConfirm")
	public String unConfirmRequest(@RequestParam("supplyOrderId") Long supplyOrderId,
			@ModelAttribute("supplyOrderQuery") SupplyOrderQuery supplyOrderQuery,
			HttpSession session, Model model) {
		UserInfo userInfo = super.getUserInfo(session);
		logger.warn(userInfo.getUserName() + "执行供货单未确认操作 供货单号 : " + supplyOrderId);
		Result<SupplyOrder> queryReuslt = supplyOrderService.getSupplyOrderById(supplyOrderId);
		if (!queryReuslt.isSuccess() || queryReuslt.getModule() == null) {
			alertError(model, "没有该供货单");
			return errorUrl;
		}

		SupplyOrder supplyOrder = queryReuslt.getModule();
		supplyOrder.setDealOperId(userInfo.getId());

		Result<Boolean> updateResult = supplyOrderService.manualUnConfirmSupplyOrder(supplyOrder);
		if (updateResult.isSuccess() && updateResult.getModule()) {
			supplyOrderQuery.setId(supplyOrderId);
			supplyOrderQuery.setSupplyStatus(Constants.SupplyOrder.STATUS_UNCONFIRMED);
			String url = returnUrl + formToParam(supplyOrderQuery);
			alertSuccessNoneBack(model);
			return url;
		} else {
			alertError(model, updateResult.getResultMsg());
		}
		return errorUrl;
	}

	private String formToParam(SupplyOrderQuery supplyOrderQuery) {
		Field[] field = SupplyOrderQuery.class.getDeclaredFields(); // 获取所有属性

		StringBuffer sb = new StringBuffer("?");
		for (int i = 0; i < field.length; i++) {
			field[i].setAccessible(true); // 设置对私有的访问权限
			try {
				if (field[i].get(supplyOrderQuery) != null
						&& !field[i].getName().equals("serialVersionUID")) {
					if (field[i].get(supplyOrderQuery) instanceof Date) {
						sb.append(field[i].getName()).append("=")
								.append(DateTool.parseDate((Date) field[i].get(supplyOrderQuery)))
								.append("&");
					} else {
						sb.append(field[i].getName()).append("=")
								.append(field[i].get(supplyOrderQuery)).append("&");
					}
				}
			} catch (IllegalArgumentException e1) {
				logger.error("formToParam error", e1);
			} catch (IllegalAccessException e1) {
				logger.error("formToParam error", e1);
			}
		}
		return sb.toString().substring(0, sb.toString().length() - 1);
	}
}
