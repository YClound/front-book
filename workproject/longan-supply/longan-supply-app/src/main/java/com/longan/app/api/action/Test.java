package com.longan.app.api.action;

import org.codehaus.jackson.map.util.JSONPObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.longan.app.api.response.Response;
import com.longan.app.common.controller.BaseController;

@Controller
public class Test extends BaseController {

	@RequestMapping(value = "test")
	public @ResponseBody
	Response onRequest() {
		Response response = new Response();
		response.setSuccess();
		return response;
	}

	@RequestMapping(value = "testJsonp")
	public @ResponseBody
	JSONPObject onRequest(@RequestParam(defaultValue = "callback") String callback) {
		Response response = new Response();
		response.setSuccess();
		return new JSONPObject(callback, response);
	}
}
