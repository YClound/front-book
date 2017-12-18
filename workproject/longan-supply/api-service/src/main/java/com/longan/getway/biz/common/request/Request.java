package com.longan.getway.biz.common.request;

import java.lang.reflect.Field;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotBlank;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotNull;
import org.springmodules.validation.bean.conf.loader.annotation.handler.RegExp;

import com.longan.biz.utils.DateTool;

public class Request {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	@NotBlank(message = "签名不能为空")
	@NotNull(message = "签名不能为空")
	public String sign;

	@NotBlank(message = "代理商编号不能为空")
	@NotNull(message = "代理商编号不能为空")
	@RegExp(value = "^\\d{1,8}$", message = "代理商编号必须是1到8位数字")
	public String userId;

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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
				logger.error("Request toString error", e1);
			} catch (IllegalAccessException e1) {
				logger.error("Request toString error", e1);
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
					logger.error("Request toString error", e1);
				} catch (IllegalAccessException e1) {
					logger.error("Request toString error", e1);
				}
			}
		}

		return sb.toString();
	}

}
