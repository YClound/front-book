package com.longan.app.common.controller;

import java.text.SimpleDateFormat;
import java.util.Date;


import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.longan.app.utils.StringEscapeEditor;

public class BaseController {
	@InitBinder
	public void formInitBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringEscapeEditor());
	}
	
	
//	public LoginUserInfo getUserInfo(HttpServletRequest request) {
//		Object object = request.getAttribute(Constants.SESSION_USER);
//		return (object == null || !(object instanceof LoginUserInfo)) ? null
//				: (LoginUserInfo) object;
//	}
}
