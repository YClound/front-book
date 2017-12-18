package com.longan.getway.biz.action.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.longan.biz.utils.SwitchUtils;
import com.longan.getway.biz.common.response.Response;

@Controller
@RequestMapping("admin")
public class Admin {
	@RequestMapping(method = RequestMethod.GET, params = "type=openCheckArea")
	public @ResponseBody
	Response openCheckArea() {
		//TODO 记录操作IP 
		SwitchUtils.setCheckAreaSwitch(true);
		Response response = new Response();
		response.setCode("00");
		response.setStatus("success");
		response.setDesc("成功");
		return response;
	}

	@RequestMapping(method = RequestMethod.GET, params = "type=closeCheckArea")
	public @ResponseBody
	Response closeCheckArea() {
		SwitchUtils.setCheckAreaSwitch(false);
		Response response = new Response();
		response.setCode("00");
		response.setStatus("success");
		response.setDesc("成功");
		return response;
	}

	@RequestMapping(method = RequestMethod.GET, params = "type=openCheckCarrier")
	public @ResponseBody
	Response openCheckCarrie() {
		SwitchUtils.setCheckCarrierTypeSwitch(true);
		Response response = new Response();
		response.setCode("00");
		response.setStatus("success");
		response.setDesc("成功");
		return response;
	}

	@RequestMapping(method = RequestMethod.GET, params = "type=closeCheckCarrier")
	public @ResponseBody
	Response closeCheckCarrie() {
		SwitchUtils.setCheckCarrierTypeSwitch(false);
		Response response = new Response();
		response.setCode("00");
		response.setStatus("success");
		response.setDesc("成功");
		return response;
	}
}
