package com.longan.getway.upstream.flow.domain;

import java.util.HashMap;
import java.util.Map;

import com.longan.biz.utils.Md5Encrypt;
import com.longan.biz.utils.Utils;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.utils.MultiThreadedHttpConnection;

public class YongLiuFlowBalance {
	private static final String url = Utils.getProperty("yongliu.url.balance");
	private static final String privateKey = Utils.getProperty("yongliu.privateKey");
	private static final String traderNo = Utils.getProperty("yongliu.userId");
	private String sign;

	public String getTraderNo() {
		return traderNo;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	private void createSign() {
		StringBuffer sb = new StringBuffer("");
		sb.append(traderNo).append(privateKey);
		this.sign = Md5Encrypt.md5(sb.toString());
	}

	public SupplyResult<String> query() {
		createSign();
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("traderNo", traderNo);
		paramMap.put("sign", sign);
		return MultiThreadedHttpConnection.getInstance().sendPostByMap(url, paramMap);
	}

}
