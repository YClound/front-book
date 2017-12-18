package com.longan.getway.upstream.callback.action;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.domain.Result;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.upstream.common.BaseCallBackController;
import com.longan.getway.upstream.common.domain.CallBack;
import com.longan.getway.upstream.flow.service.impl.LTShiKeFlowSupplyServiceImpl;
import com.longan.getway.upstream.flow.service.impl.RuiyiFlowSupplyImpl;
import com.longan.getway.upstream.flow.vo.LTShiKeFlowTransVO;
import com.longan.getway.upstream.flow.vo.RuiyiFlowTransVO;
import com.longan.getway.utils.Base64Utils;

public class LTShikeFlowCallback extends BaseCallBackController{
	private String responseSuccess="0";
	@RequestMapping(value = "callback/LTShikeFlowCallback" ,method = RequestMethod.POST)
	@ResponseBody
	public void onRequest(HttpServletRequest request, HttpServletResponse response) {
		logCallBack(request, "联通时科流量充值");
		final String orderNo = request.getParameter("orderNo");
		final String code = request.getParameter("code");
		final String desc = request.getParameter("desc");
		
		//判空
		if(isExistBlank(orderNo,code,desc)){
			//参数不完整 
			logger.warn("联通时科流量充值回调：参数不完整。 ip : " + getRemoteIp(request));
			responseStr(response, "error");
			return;
		}
		callbackExecutor.execute(new Runnable() {
			@Override
			public void run() {
				Result<SupplyOrder> supplyOrderResult = supplyOrderService
						.getSupplyOrderByUpstreamSerialno(orderNo, LTShiKeFlowTransVO.traderId);
				if (!supplyOrderResult.isSuccess()) {
					logger.error("联通时科流量充值回调,获取供货单失败  上游订单:  " + orderNo + " msg: "
							+ supplyOrderResult.getResultMsg());
					return;
				}
				SupplyOrder supplyOrder = supplyOrderResult.getModule();
				if (supplyOrder == null) {
					logger.error("联通时科流量充值回调,获取供货单为空 supplyOrder:  " + supplyOrder + " msg: "
							+ supplyOrderResult.getResultMsg());
					return;
				}
				//
				CallBack upstreamCallBack = new CallBack();
				upstreamCallBack.setSupplyOrderId(supplyOrder.getId());
				if(LTShiKeFlowSupplyServiceImpl.success_code.equals(code)){
					upstreamCallBack.setStatus(SupplyResult.STATUS_SUCCESS);
				}else{
					upstreamCallBack.setStatus(SupplyResult.STATUS_FAILED);
					String msg ="Base64解析失败";
					try {
						msg = new String(Base64Utils.decode(desc),"GBK");
					} catch (Exception e) {
						e.printStackTrace();
					}
					upstreamCallBack.setFailedMsg(msg);
				}
				// 处理订单 ,退款，更新等操作
				bizDealService.dealBizOrder(supplyOrder, upstreamCallBack);
			}
		});
		responseStr(response, responseSuccess);
	}
	private boolean isExistBlank(String ... ss) {
		for(String s : ss){
			if(s == null || s.isEmpty())return true ;
		}
		return false ;
	}
}
