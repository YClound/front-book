package com.longan.getway.remoteserver.impl;

import com.longan.biz.dataobject.BizOrder;
import com.longan.biz.utils.Md5Encrypt;

public class NewCallBackHelper {
	public static String createNewCallBackUrl(String callBackUrl,BizOrder bizOrder,String key){
		String userId = bizOrder.getUserId().toString();
		String service_no=bizOrder.getBizId().toString();
		String service_order=bizOrder.getId().toString();
		String order_no=bizOrder.getDownstreamSerialno();
		String status = bizOrder.getStatus().toString();
		//生成sign
		String sign = Md5Encrypt.md5(order_no+service_no+service_order+status+userId+key);
		return callBackUrl+"userId="+userId+"&service_no="+service_no+"&service_order"+service_order+"&order_no"+order_no+"&status"+status+"&sign="+sign;
	}
}
