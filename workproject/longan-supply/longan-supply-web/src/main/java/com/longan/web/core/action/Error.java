package com.longan.web.core.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class Error {

	@RequestMapping(value = "error404", method = RequestMethod.GET)
	public void requestNotFound() {

	}

	@RequestMapping(value = "refuse", method = RequestMethod.GET)
	public void requestRefuse() {

	}
}
