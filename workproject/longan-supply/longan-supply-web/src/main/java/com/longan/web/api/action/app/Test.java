package com.longan.web.api.action.app;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.util.JSONPObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.longan.biz.dataobject.Item;
import com.longan.web.api.response.AjaxResponse;
import com.longan.web.common.controller.BaseController;

@Controller
public class Test extends BaseController {

	@RequestMapping(value = "api/app/test")
	public @ResponseBody
	AjaxResponse onRequest() {
		AjaxResponse response = new AjaxResponse();
		response.setSuccess();
		List<Integer> list = new ArrayList<Integer>();
		list.add(10);
		list.add(11);
		list.add(12);
		list.add(13);
		list.add(14);
		response.setModule(list);
		return response;
	}

	@RequestMapping(value = "api/app/testJsonp")
	public @ResponseBody
	JSONPObject onRequest(
			@RequestParam(defaultValue = "JSON_CALLBACK", required = false) String callback) {
		AjaxResponse response = new AjaxResponse();
		response.setSuccess();
		// List<Integer> list = new ArrayList<Integer>();
		// list.add(10);
		// list.add(11);
		// list.add(12);
		// list.add(13);
		// list.add(14);

		List<Item> list = new ArrayList<Item>();

		Item item1 = new Item();
		item1.setId(1);
		item1.setItemName("商品1");

		Item item2 = new Item();
		item2.setId(2);
		item2.setItemName("商品2");

		list.add(item1);
		list.add(item2);
		response.setModule(list);

		return new JSONPObject(callback, response);
	}
}
