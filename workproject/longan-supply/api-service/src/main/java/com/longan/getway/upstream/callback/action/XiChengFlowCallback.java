package com.longan.getway.upstream.callback.action;

import java.io.StringReader;
import java.net.URLDecoder;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.domain.Result;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.upstream.common.BaseCallBackController;
import com.longan.getway.upstream.common.domain.CallBack;

@Controller
public class XiChengFlowCallback extends BaseCallBackController {
	@RequestMapping(value = "callback/xiChengFlowCallback")
	@ResponseBody
	public void onRequest(@RequestBody String requestBody,HttpServletRequest request, HttpServletResponse response) {
		try {
			requestBody = URLDecoder.decode(requestBody, "utf-8");
			logger.warn("收到 ip : " + getRemoteIp(request) + "西城流量充值的结果通知请求: " + requestBody);
			final String stringBody = requestBody;
			callbackExecutor.execute(new Runnable() {
				@Override
				public void run() {
					SAXReader reader = new SAXReader();
					try {
						Document document = reader.read(new StringReader(stringBody));
						Element root = document.getRootElement();
						Element body = root.element("body");
						Iterator<Element> items = body.elementIterator("item");
						while(items.hasNext()){
							Element item = items.next();
							String orderId = item.elementText("orderId");
							Result<SupplyOrder> supplyOrderResult = supplyOrderService.getSupplyOrder(Long.valueOf(orderId));
							if (!supplyOrderResult.isSuccess()) {
								logger.error("西城流量充值回调,获取供货单失败  上游订单号:  " + orderId + " msg: " + supplyOrderResult.getResultMsg());
								return;
							}
							SupplyOrder supplyOrder = supplyOrderResult.getModule();
							if (supplyOrder == null) {
								logger.error("西城流量充值回调,获取供货单为空 上游订单号:  " + orderId + " msg: " + supplyOrderResult.getResultMsg());
								return;
							}
							CallBack upstreamCallBack = new CallBack();
							upstreamCallBack.setSupplyOrderId(supplyOrder.getId());
							String result = item.elementText("result");
							if("0000".equals(result)){//成功了
								upstreamCallBack.setStatus(SupplyResult.STATUS_SUCCESS);
							}else if("1003".equals(result)){
								upstreamCallBack.setStatus(SupplyResult.STATUS_FAILED);
								upstreamCallBack.setFailedCode(result);
								String msg = item.elementText("desc");
								Element errorCodeEle = item.element("gateErrorCode");
								msg = errorCodeEle== null ? msg:msg+"|"+errorCodeEle.getText();
								upstreamCallBack.setFailedMsg(msg);
							}else{
								String msg = item.elementText("desc");
								upstreamCallBack.setStatus(SupplyResult.STATUS_FAILED);
								upstreamCallBack.setFailedCode(result);
								upstreamCallBack.setFailedMsg(msg);
							}
							// 处理订单 ,退款，更新等操作
							bizDealService.dealBizOrder(supplyOrder, upstreamCallBack);
						}		
					} catch (DocumentException e) {
						logger.error("西城流量充值的结果通知请求处理异常", e);
						e.printStackTrace();
					}
				}
			});
		} catch (Exception e) {
			logger.error("西城流量充值的结果通知请求处理异常", e);
			e.printStackTrace();
			responseStr(response, createResponse("0010","处理异常"));
			return ;
		}
		// 返回成功
		responseStr(response, createResponse("0000","处理成功"));
	}
	private String createResponse(String result,String desc){
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding("utf-8");
		Element root = doc.addElement("response");
		root.addElement("result").setText(result);
		root.addElement("desc").setText(desc);;
		return doc.asXML();
	}
}
