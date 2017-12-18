package com.longan.getway.biz.common.response;

import java.lang.reflect.Field;
import java.util.Date;

import com.longan.biz.utils.DateTool;

public class BaseData {

	public String toString() {
		Field[] fields = this.getClass().getDeclaredFields(); // 获取所有属性

		StringBuffer sb = new StringBuffer("[");
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
		sb.append("]");
		return sb.toString();
	}
}
