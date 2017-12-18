package com.longan.getway.upstream.callback.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.domain.Result;
import com.longan.biz.utils.Md5Encrypt;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.upstream.common.BaseCallBackController;
import com.longan.getway.upstream.common.domain.CallBack;
import com.longan.getway.upstream.flow.vo.MiaoxunFlowVO;
@Controller
public class MiaoxunFlowCallback  extends BaseCallBackController{
	@RequestMapping(value = "callback/miaoxunFlowCallback.do",method = RequestMethod.POST)
	public void onRequest(HttpServletRequest request, HttpServletResponse response) {
		logCallBack(request, "妙讯流量充值");
		final String retcode = request.getParameter("retcode");
		final String orderid = request.getParameter("orderid");
		final String tradeno = request.getParameter("tradeno");
		String mobile = request.getParameter("mobile");
		String sign = request.getParameter("sign");
		final String retmsg = request.getParameter("retmsg");
		
		String signStr=String.format("retcode=%s&orderid=%s&tradeno=%s&mobile=%s&key=%s",retcode,orderid,tradeno,mobile,Md5Encrypt.md5(MiaoxunFlowVO.secretkey));
		String sign2 = Md5Encrypt.md5(signStr);
		if(!sign.equals(sign2)){
			responseStr(response, "sign error");
			return ;
		}
		
		callbackExecutor.execute(new Runnable() {
			@Override
			public void run() {
				Result<SupplyOrder> supplyOrderResult = supplyOrderService.getSupplyOrderById(Long.valueOf(orderid));
				if (!supplyOrderResult.isSuccess()) {
					logger.error("妙讯流量充值回调,获取供货单失败 供货单号:  " + orderid + " msg: "
							+ supplyOrderResult.getResultMsg());
					return;
				}
				SupplyOrder supplyOrder = supplyOrderResult.getModule();
				if (supplyOrder == null) {
					logger.error("妙讯流量充值回调,获取供货单为空 supplyOrder:  " + supplyOrder + " msg: "
							+ supplyOrderResult.getResultMsg());
					return;
				}
				supplyOrder.setUpstreamSerialno(tradeno);
				CallBack upstreamCallBack = new CallBack();
				upstreamCallBack.setSupplyOrderId(supplyOrder.getId());
				if("20".equals(retcode)){
					upstreamCallBack.setStatus(SupplyResult.STATUS_SUCCESS);
				}else if(!"10".equals(retcode)){
					upstreamCallBack.setStatus(SupplyResult.STATUS_FAILED);
					upstreamCallBack.setFailedMsg(retmsg);
				}else{
					// 其他状态
					logger.error("非终极状态不做处理 状态： " + retcode);
					return;
				}
				// 处理订单 ,退款，更新等操作
				bizDealService.dealBizOrder(supplyOrder, upstreamCallBack);
			}
		});
		responseStr(response, "ok");
		
	}
	
}
