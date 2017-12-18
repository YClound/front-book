package com.longan.app.utils;

import java.beans.PropertyEditorSupport;

import org.apache.commons.lang.StringEscapeUtils;

public class StringEscapeEditor extends PropertyEditorSupport {
	private boolean escapeSQL = true;
	private boolean cleanXSS = true;

	public StringEscapeEditor() {
		super();
	}

	public StringEscapeEditor(boolean cleanXSS, boolean escapeSQL) {
		super();
		this.cleanXSS = cleanXSS;
		this.escapeSQL = escapeSQL;
	}

	@Override
	public void setAsText(String text) {
		if (text == null) {
			setValue(null);
		} else {
			String value = text;
			if (escapeSQL) {
				value = StringEscapeUtils.escapeSql(value);
			}
			if (cleanXSS) {
				value = cleanXSS(value);
			}
			setValue(value);
		}
	}

	@Override
	public String getAsText() {
		Object value = getValue();
		return value != null ? value.toString() : "";
	}

	private static String cleanXSS(String value) {
		// You'll need to remove the spaces from the html entities below
		value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
		value = value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
		value = value.replaceAll("'", "&#39;");
		value = value.replaceAll("eval\\((.*)\\)", "");
		value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
		value = value.replaceAll("script", "");
		return value;
	}

	public static void main(String[] args) {
		String value = "测试";
		// value = StringEscapeUtils.escapeHtml(value);
		// value = StringEscapeUtils.escapeJavaScript(value);
		value = StringEscapeUtils.escapeSql(value);
		value = cleanXSS(value);
		// System.out.println(value);
	}
}
