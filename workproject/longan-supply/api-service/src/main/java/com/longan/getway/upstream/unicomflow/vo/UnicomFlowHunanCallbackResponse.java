package com.longan.getway.upstream.unicomflow.vo;

import java.lang.reflect.Field;
import java.util.Date;

import com.longan.biz.utils.DateTool;

public class UnicomFlowHunanCallbackResponse {
	private String code;
	private String msg;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
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
