package com.longan.getway.upstream.callback.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.domain.Result;
import com.longan.biz.utils.Md5Encrypt;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.upstream.common.BaseCallBackController;
import com.longan.getway.upstream.common.domain.CallBack;
import com.longan.getway.upstream.flow.vo.DingxinFlowTransVO;
@Controller
public class DingxinFlowCallback extends BaseCallBackController {
	private String responseSuccess="OK";
	@RequestMapping(value = "callback/dingxinFlowCallback.do")
	@ResponseBody
	public void onRequest(HttpServletRequest request, HttpServletResponse response) {
		logCallBack(request, "鼎信流量");
		String userId = request.getParameter(DingxinFlowTransVO.PARAM_USERID);
		final String orderId = request.getParameter(DingxinFlowTransVO.PARAM_ORDERID);
		final String state = request.getParameter(DingxinFlowTransVO.PARAM_STATE);
		String sign = request.getParameter(DingxinFlowTransVO.PARAM_USERKEY);
		String uid = request.getParameter(DingxinFlowTransVO.PARAM_ACCOUNT);
		
		//判空
		if(!isAllNotBlank(userId,orderId,state,sign)){
			//参数不完整 
			logger.warn("鼎信流量充值回调：参数不完整。 ip : " + getRemoteIp(request) + responseSuccess);
			responseStr(response, responseSuccess);
			return;
		}
		//验签
//		if(!checkSign(orderId,state,sign)){
//			//参数不完整 
//			logger.warn("鼎信流量充值回调：验签失败。 ip : " + getRemoteIp(request) + responseSuccess);
//			responseStr(response, responseSuccess);
//			return;
//		}
		callbackExecutor.execute(new Runnable() {
			@Override
			public void run() {
				Long supplyOrderId = Long.parseLong(orderId);
				Result<SupplyOrder> supplyOrderResult = supplyOrderService
						.getSupplyOrderByIdAndTraderId(supplyOrderId, DingxinFlowTransVO.traderId);
				if (!supplyOrderResult.isSuccess()) {
					logger.error("鼎信流量充值回调,获取供货单失败  supplyOrderId:  " + supplyOrderId + " msg: "
							+ supplyOrderResult.getResultMsg());
					return;
				}
				SupplyOrder supplyOrder = supplyOrderResult.getModule();
				if (supplyOrder == null) {
					logger.error("鼎信流量充值回调,获取供货单为空 supplyOrder:  " + supplyOrder + " msg: "
							+ supplyOrderResult.getResultMsg());
					return;
				}
				//
				CallBack upstreamCallBack = new CallBack();
				upstreamCallBack.setSupplyOrderId(supplyOrder.getId());
				if(DingxinFlowTransVO.isChargeSuccess(state)){
					upstreamCallBack.setStatus(SupplyResult.STATUS_SUCCESS);
				}else if(DingxinFlowTransVO.isChargeFailed(state)){
					upstreamCallBack.setStatus(SupplyResult.STATUS_FAILED);
					upstreamCallBack.setFailedMsg(state + "_充值失败");
				}else{
					// 其他状态
					logger.error("鼎信流量充值回调非终极状态不做处理 状态： " + state);
					return;
				}
				// 处理订单 ,退款，更新等操作
				bizDealService.dealBizOrder(supplyOrder, upstreamCallBack);
			}
		});

		// 返回成功
		logger.warn("返回 ip : " + getRemoteIp(request) + responseSuccess);
		responseStr(response, responseSuccess);
	}
	
	/**
	 * 验证返回签名是否正确
	 * @param orderId
	 * @param state
	 * @param sign
	 * @return
	 */
	private boolean checkSign(String orderId,String state,String sign){
		StringBuffer sb = new StringBuffer();
		sb.append(DingxinFlowTransVO.PARAM_USERID).append(DingxinFlowTransVO.userId)
		.append(DingxinFlowTransVO.PARAM_PWD).append(DingxinFlowTransVO.pwd)
		.append(DingxinFlowTransVO.PARAM_ORDERID).append(orderId)
		.append(DingxinFlowTransVO.PARAM_STATE).append(state)
		.append(DingxinFlowTransVO.PARAM_ACCOUNT).append("null")
		.append(DingxinFlowTransVO.privateKey);
		if(sign.equals(Md5Encrypt.md5(sb.toString()).toUpperCase())){
			return true ;
		}
		return  false ;
	}
	private boolean isAllNotBlank(String ... ss){
		for(String s : ss){
			if(StringUtils.isBlank(s))return false ;
		}
		return true  ;
	}
}
