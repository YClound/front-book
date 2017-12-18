package com.longan.getway.upstream.cardforward.vo;

import java.util.HashMap;
import java.util.Map;

import com.longan.biz.utils.Md5Encrypt;
import com.longan.biz.utils.Utils;

public class WtjQueryRequestVO extends WtjBaseRequestVO {
	private static final String queryUrl = URL + Utils.getProperty("wtjCardForward.queryAction");

	private String searchType = "3"; // 按我方订单号查询
	private String billId;
	private String billDate;
	private static final Integer pageSize = 10;
	private static final Integer pageIndex = 1;

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public String getBillId() {
		return billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}

	public String getBillDate() {
		return billDate;
	}

	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}

	public void createSign() {
		// search_type=***&bill_id=***&bill_date=***&page_size=***&page_index=***&client_ip=***&time_stamp=***|||secKey
		String source = "search_type=" + searchType + "&bill_id=" + billId + "&bill_date="
				+ billDate + "&page_size=" + pageSize + "&page_index=" + pageIndex + "&client_ip="
				+ getClientIp() + "&time_stamp=" + getTimeStamp() + "|||" + getSecKey();
		String signStr = Md5Encrypt.md5(source);
		setSign(signStr);
	}

	public String createUrl() {
		return queryUrl;
	}

	public Map<String, String> createParams() {
		Map<String, String> result = new HashMap<String, String>();
		// 设置签名
		createSign();

		result.put("search_type", searchType);
		result.put("bill_id", billId);
		result.put("bill_date", billDate);
		result.put("page_size", pageSize + "");
		result.put("page_index", pageIndex + "");
		result.put("client_ip", getClientIp());
		result.put("time_stamp", getTimeStamp());
		result.put("sign", getSign());
		return result;
	}
}
