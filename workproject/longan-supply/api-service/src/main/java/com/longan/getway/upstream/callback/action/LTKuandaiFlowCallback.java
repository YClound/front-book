package com.longan.getway.upstream.callback.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.domain.Result;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.upstream.common.BaseCallBackController;
import com.longan.getway.upstream.common.domain.CallBack;
import com.longan.getway.upstream.flow.vo.LTKuandaiFlowVO;
@Controller
public class LTKuandaiFlowCallback extends BaseCallBackController{
	@RequestMapping(value = "callback/LTKuandaiFlowCallback" ,method = RequestMethod.POST)
	@ResponseBody
	public void onRequest(HttpServletRequest request, HttpServletResponse response) {
		logCallBack(request, "联通宽带流量充值");
		final String orderId = request.getParameter("orderId");
		final String partnerOrderNo = request.getParameter("transNo");
		final String retCode = request.getParameter("respCode");
		callbackExecutor.execute(new Runnable() {
			@Override
			public void run() {
				Long supplyOrderId = Long.parseLong(partnerOrderNo);
				Result<SupplyOrder> supplyOrderResult = supplyOrderService
						.getSupplyOrderByIdAndTraderId(supplyOrderId,LTKuandaiFlowVO.traderId);
				if (!supplyOrderResult.isSuccess()) {
					logger.error("联通宽带流量充值回调,获取供货单失败  supplyOrderId:  " + supplyOrderId + " msg: "
							+ supplyOrderResult.getResultMsg());
					return;
				}
				SupplyOrder supplyOrder = supplyOrderResult.getModule();
				if (supplyOrder == null) {
					logger.error("联通宽带流量充值回调,获取供货单为空 supplyOrder:  " + supplyOrder + " msg: "
							+ supplyOrderResult.getResultMsg());
					return;
				}
				if(StringUtils.isNotBlank(orderId))supplyOrder.setUpstreamSerialno(orderId);
				CallBack upstreamCallBack = new CallBack();
				upstreamCallBack.setSupplyOrderId(supplyOrder.getId());
				if("0000".equals(retCode)){
					upstreamCallBack.setStatus(SupplyResult.STATUS_SUCCESS);
				}else if(!"0001".equals(retCode) && !"3011".equals(retCode) && !"9001".equals(retCode)
						&& !"9002".equals(retCode) && !"9102".equals(retCode)){
					upstreamCallBack.setStatus(SupplyResult.STATUS_FAILED);
					upstreamCallBack.setFailedMsg(LTKuandaiFlowVO.codeResultMap.get(retCode));
				}else{
					// 其他状态
					logger.error("非终极状态不做处理 状态： " + retCode);
					return;
				}
				// 处理订单 ,退款，更新等操作
				bizDealService.dealBizOrder(supplyOrder, upstreamCallBack);
			}
		});
		responseStr(response, "success");
	}
}
