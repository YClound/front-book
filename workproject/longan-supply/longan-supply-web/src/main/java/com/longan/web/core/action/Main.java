package com.longan.web.core.action;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.longan.biz.core.AcctService;
import com.longan.biz.core.QueryService;
import com.longan.biz.dataobject.AcctInfo;
import com.longan.biz.dataobject.BizOrder;
import com.longan.biz.dataobject.BizOrderQuery;
import com.longan.biz.dataobject.UserInfo;
import com.longan.biz.domain.Result;
import com.longan.web.biz.domain.LoginUserInfo;
import com.longan.web.common.controller.BaseController;

@Controller
public class Main extends BaseController {
	@Resource
	private AcctService acctService;
	@Resource
	private QueryService queryService;

	@RequestMapping("main")
	public void main(HttpServletRequest request, Model model) {
		LoginUserInfo loginUserInfo = super.getUserInfo(request);
		UserInfo userInfo = loginUserInfo.getUserInfo();
		// 账户信息
		if (userInfo.getAcctId() != null) {
			Result<AcctInfo> acctInfoResult = acctService.getAcctInfo(userInfo.getAcctId());
			model.addAttribute("acctInfo", acctInfoResult.getModule());
		}
		// 订单信息
		Calendar calendar = Calendar.getInstance();
		Date endGmtCreate = calendar.getTime();
		calendar.add(Calendar.DAY_OF_YEAR, -31);// 当前时间前31天
		Date beginGmtCreate = calendar.getTime();
		BizOrderQuery bizOrderQuery = new BizOrderQuery();
		bizOrderQuery.setStartGmtCreate(beginGmtCreate);
		bizOrderQuery.setEndGmtCreate(endGmtCreate);
		bizOrderQuery.setUserId(userInfo.getId());
		bizOrderQuery.setPageSize(5);
		Result<List<BizOrder>> result = queryService.queryBizOrderPage(bizOrderQuery);
		model.addAttribute("bizOrderList", result.getModule());
	}
	
	@RequestMapping("forWaiting")
	public void forWaiting(HttpServletRequest request, Model model) {
		LoginUserInfo loginUserInfo = super.getUserInfo(request);
		UserInfo userInfo = loginUserInfo.getUserInfo();
		model.addAttribute("userInfo", userInfo);
	}
}
