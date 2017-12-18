package com.longan.web.api.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.longan.web.api.response.AjaxResponse;
import com.longan.web.common.controller.BaseController;

@Controller
public class Check extends BaseController {
	@RequestMapping(value = "api/check", method = RequestMethod.GET)
	public @ResponseBody
	AjaxResponse testRequest() {
		AjaxResponse ajaxResponse = new AjaxResponse();
		ajaxResponse.setSuccess();
		return ajaxResponse;
	}
}
