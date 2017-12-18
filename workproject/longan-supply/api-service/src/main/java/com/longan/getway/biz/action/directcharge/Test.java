package com.longan.getway.biz.action.directcharge;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.longan.getway.biz.common.response.BuyResponse;
import com.longan.getway.biz.common.response.Response;

@Controller
@RequestMapping("test")
public class Test {
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody Response onResut() {
		BuyResponse response = new BuyResponse();
		response.setCode("00");
		response.setStatus("success");
		response.setDesc("成功");
		return response;
	}
}
