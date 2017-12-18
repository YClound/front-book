package com.longan.getway.upstream.callback.action;

import java.io.IOException;
import java.io.StringReader;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.longan.biz.core.ShortMsgService;
import com.longan.biz.dataobject.ShortMsg;
import com.longan.biz.utils.Constants;
import com.longan.getway.upstream.common.BaseCallBackController;

@Controller
public class BaiWuShortMsgCallback extends BaseCallBackController {
	@Resource
	private ShortMsgService shortMsgService;

	private final static String responseSuccess = "0";

	@RequestMapping(value = "callback/baiWuShortMsgCallback")
	@ResponseBody
	public void onRequest(HttpServletRequest request, HttpServletResponse response) {
		try {
			final String stringBody = super.getRequestBody(request);
			callbackExecutor.execute(new Runnable() {
				@Override
				public void run() {
					SAXReader reader = new SAXReader();

					try {
						Document document = reader.read(new StringReader(stringBody));
						Element root = document.getRootElement();
						Element report = root.element("report");
						String msg_id = report.elementText("msg_id");
						String err = report.elementText("err");
						ShortMsg shortMsg = new ShortMsg();
						shortMsg.setId(Long.parseLong(msg_id));
						if ("0".equals(err)) {// 发送成功
							shortMsg.setStatus(Constants.ShortMsg.STATUS_SUCCESS);
							shortMsgService.updateShortMsg(shortMsg);
						} else {
							shortMsg.setStatus(Constants.ShortMsg.STATUS_FAILED);
							shortMsgService.updateShortMsg(shortMsg);
						}
					} catch (DocumentException e) {
						logger.error("parse BaiWuShortMsgCallback document error", e);
					}
				}
			});

			logger.warn("收到百悟网关, ip : " + getRemoteIp(request) + " 的结果通知请求: " + stringBody);
		} catch (IOException e) {
			logger.error("baiWuShortMsgCallback get data error ", e);
			responseStr(response, responseSuccess);
		}

		// 返回成功
		logger.warn("返回 ip : " + getRemoteIp(request) + responseSuccess);
		responseStr(response, responseSuccess);
	}
}
