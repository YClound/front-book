package com.longan.getway.upstream.telephone.vo;

import com.thoughtworks.xstream.XStream;

public class YiSaiCallbackResponse {
	private final static String preFix = "<?xml version=\"1.0\" encoding=\" GB2312\"?>\n";
	private String result = "success";

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public void setError() {
		result = "fail";
	}

	public String toString() {
		XStream xstream = new XStream();
		xstream.autodetectAnnotations(true);
		xstream.alias("root", YiSaiCallbackResponse.class);
		return preFix + xstream.toXML(this);
	}
}
