package com.longan.getway.upstream.callback.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import antlr.StringUtils;

import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.domain.Result;
import com.longan.client.remote.domain.ChargeInfo;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.upstream.common.BaseCallBackController;
import com.longan.getway.upstream.common.domain.CallBack;
import com.longan.getway.upstream.flow.service.impl.RuiyiFlowSupplyImpl;
import com.longan.getway.upstream.flow.vo.DingxinFlowTransVO;
import com.longan.getway.upstream.flow.vo.RuiyiFlowTransVO;
@Controller
public class RuiyiFlowCallback extends BaseCallBackController{
	private String responseSuccess="success";
	@RequestMapping(value = "callback/ruiyiFlowCallback" ,method = RequestMethod.POST)
	@ResponseBody
	public void onRequest(HttpServletRequest request, HttpServletResponse response) {
		logCallBack(request, "瑞翼流量充值");
		final String orderNo = request.getParameter("orderNo");
		final String partnerOrderNo = request.getParameter("partnerOrderNo");
		final String status = request.getParameter("status");
		final String desc = request.getParameter("desc");
		//判空
		if(!isAllNotBlank(orderNo,partnerOrderNo,status)){
			//参数不完整 
			logger.warn("瑞翼流量充值回调：参数不完整。 ip : " + getRemoteIp(request));
			responseStr(response, "error");
			return;
		}
		callbackExecutor.execute(new Runnable() {
			@Override
			public void run() {
				Long supplyOrderId = Long.parseLong(partnerOrderNo);
				Result<SupplyOrder> supplyOrderResult = supplyOrderService
						.getSupplyOrderByIdAndTraderId(supplyOrderId, RuiyiFlowTransVO.traderId);
				if (!supplyOrderResult.isSuccess()) {
					logger.error("瑞翼流量充值回调,获取供货单失败  supplyOrderId:  " + supplyOrderId + " msg: "
							+ supplyOrderResult.getResultMsg());
					return;
				}
				SupplyOrder supplyOrder = supplyOrderResult.getModule();
				if (supplyOrder == null) {
					logger.error("瑞翼流量充值回调,获取供货单为空 supplyOrder:  " + supplyOrder + " msg: "
							+ supplyOrderResult.getResultMsg());
					return;
				}
				//
				CallBack upstreamCallBack = new CallBack();
				upstreamCallBack.setSupplyOrderId(supplyOrder.getId());
				//接口文档建议查一次，那我就查一次
				RuiyiFlowTransVO vo = RuiyiFlowTransVO.createQueryOrderVO(String.valueOf(supplyOrder.getId()));
				SupplyResult<String> queryResult = vo.sendQueryOrder();
				String resultStatus = status;
				String msg = desc;
				if(queryResult.isSuccess()){
					JSONObject jo = JSONObject.fromObject(queryResult.getModule());
					String code = jo.getString(RuiyiFlowSupplyImpl.RES_CODE);
					if(RuiyiFlowSupplyImpl.isSuccess(code)){
						String data = jo.getString(RuiyiFlowSupplyImpl.RES_DATA);
						JSONObject dataJO = JSONObject.fromObject(data);
						resultStatus = dataJO.getString("status");
					}else{
						msg = jo.getString(RuiyiFlowSupplyImpl.RES_MSG);
					}
				}
				if("1".equals(resultStatus)){//成功
					upstreamCallBack.setStatus(SupplyResult.STATUS_SUCCESS);
				}else if("2".equals(resultStatus)){//失败
					upstreamCallBack.setStatus(SupplyResult.STATUS_FAILED);
					upstreamCallBack.setFailedMsg(msg);
				}else{
					logger.error("瑞翼流量充值回调非终极状态不做处理 状态： " + resultStatus);
					return;
				}
				// 处理订单 ,退款，更新等操作
				bizDealService.dealBizOrder(supplyOrder, upstreamCallBack);
			}
		});
		responseStr(response, responseSuccess);
	}
	private boolean isAllNotBlank(String ... ss){
		for(String s : ss){
			if(s == null || s.trim().length()<1)return false ;
		}
		return true  ;
	}
}
