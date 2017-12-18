package com.longan.getway.biz.common.response;

import java.lang.reflect.Field;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import com.longan.biz.utils.Constants;
import com.longan.biz.utils.DateTool;

@XmlRootElement
public class Response {
	private String status = Constants.ErrorCode.FAILED;
	private String desc;
	private String code;

	public void setSuccess() {
		setStatus(Constants.ErrorCode.SUCCESS);
		setDesc(Constants.ErrorCode.DESC_SUCCESS);
		setCode(Constants.ErrorCode.CODE_SUCCESS);
	}

	public boolean isSuccess() {
		return "success".equals(status);
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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
