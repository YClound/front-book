package com.longan.getway.upstream.telecomflow.vo;

import java.lang.reflect.Field;
import java.util.Date;

import com.longan.biz.utils.DateTool;

public class HcTelecomFlowCallbackResponse {
	private String handleResult = "T";
	private String orderNo;

	private String failedCode;
	private String failedReason;

	public void setError() {
		handleResult = "F";
	}

	public String getHandleResult() {
		return handleResult;
	}

	public void setHandleResult(String handleResult) {
		this.handleResult = handleResult;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getFailedCode() {
		return failedCode;
	}

	public void setFailedCode(String failedCode) {
		this.failedCode = failedCode;
	}

	public String getFailedReason() {
		return failedReason;
	}

	public void setFailedReason(String failedReason) {
		this.failedReason = failedReason;
	}

	public String toString() {
		Field[] fields = this.getClass().getDeclaredFields(); // 获取所有属性

		StringBuffer sb = new StringBuffer("");
		for (Field filed : fields) {
			filed.setAccessible(true); // 设置对私有的访问权限
			try {
				if (filed.get(this) != null && !filed.getName().equals("serialVersionUID")) {
					if (filed.get(this) instanceof Date) {
						sb.append(filed.getName()).append("=")
								.append(DateTool.parseDate((Date) filed.get(this))).append("&");
					} else {
						sb.append(filed.getName()).append("=").append(filed.get(this)).append("&");
					}
				}
			} catch (IllegalArgumentException e1) {
			} catch (IllegalAccessException e1) {
			}
		}

		if (this.getClass().getGenericSuperclass() != null) {
			Class<?> superClass = this.getClass().getSuperclass();// 父类
			Field[] superFields = superClass.getDeclaredFields();// 父类变量
			for (Field filed : superFields) {
				filed.setAccessible(true); // 设置对私有的访问权限
				try {
					if (filed.get(this) != null && !filed.getName().equals("logger")) {
						if (filed.get(this) instanceof Date) {
							sb.append(filed.getName()).append("=")
									.append(DateTool.parseDate((Date) filed.get(this))).append("&");
						} else {
							sb.append(filed.getName()).append("=").append(filed.get(this))
									.append("&");
						}
					}
				} catch (IllegalArgumentException e1) {
				} catch (IllegalAccessException e1) {
				}
			}
		}

		return sb.toString();
	}

}
