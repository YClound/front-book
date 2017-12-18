package com.longan.getway.upstream.callback.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;



import com.longan.getway.upstream.common.BaseCallBackController;

public class BaoZiFlowCallback extends BaseCallBackController{
	@RequestMapping(value = "callback/baozi", method = RequestMethod.GET)
	public void onRequest(String userId,String service_no,String service_order,String order_no,String status,String sign,HttpServletRequest request, HttpServletResponse response) {
		System.out.println("充值回调");
		System.out.println("userId="+userId);
		System.out.println("service_no="+service_no);
		System.out.println("service_order="+service_order);
		System.out.println("order_no="+order_no);
		System.out.println("status="+status);
		System.out.println("sign="+sign);
		System.out.println("充值回调结束");
	}
}
