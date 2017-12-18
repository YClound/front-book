package com.longan.web.core.action.funds;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.longan.biz.core.QueryService;
import com.longan.biz.dataobject.AcctLog;
import com.longan.biz.dataobject.AcctLogQuery;
import com.longan.biz.dataobject.UserInfo;
import com.longan.biz.domain.Result;
import com.longan.biz.utils.Constants;
import com.longan.biz.utils.DateTool;
import com.longan.web.biz.domain.LoginUserInfo;
import com.longan.web.common.controller.BaseController;
import com.longan.web.core.form.AcctLogForm;

@Controller
@RequestMapping("funds/queryAcctLog")
public class QueryAcctLog extends BaseController {
	@Resource
	private QueryService queryService;

	@RequestMapping(method = RequestMethod.GET)
	public void index(@ModelAttribute("acctLogQuery") AcctLogForm acctLogForm,
			HttpServletRequest request, Model model) {
		LoginUserInfo loginUserInfo = super.getUserInfo(request);
		UserInfo userInfo = loginUserInfo.getUserInfo();
		acctLogForm.setUserId(userInfo.getId());// 只能看自己的订单
		if (StringUtils.isNotEmpty(acctLogForm.getId())) {
			acctLogForm.setEndGmtCreate(null);
			acctLogForm.setStartGmtCreate(null);
		} else {
			Date date = null;
			try {
				date = DateTool.strintToDate(DateTool.parseDate(new Date()));
			} catch (ParseException e) {
			}
			if (acctLogForm.getEndGmtCreate() == null) {
				acctLogForm.setEndGmtCreate(date);
			}
			if (acctLogForm.getStartGmtCreate() == null) {
				acctLogForm.setStartGmtCreate(date);
			}
			if (!check2Date(acctLogForm.getStartGmtCreate(), acctLogForm.getEndGmtCreate())) {
				super.alertErrorNoneBack(model, "订单时间区间不能超过2个月");
			}
		}
		AcctLogQuery acctLogQuery = formToAcctLogQuery(acctLogForm);
		Result<List<AcctLog>> result = queryService.queryAcctLog(acctLogQuery);
		acctLogForm.setTotalItem(acctLogQuery.getTotalItem());
		model.addAttribute("acctLogList", result.getModule());
	}

	@RequestMapping(method = RequestMethod.POST)
	public void doQuery(@ModelAttribute("acctLogQuery") AcctLogForm acctLogForm,
			BindingResult bindingResult, HttpServletRequest request, Model model) {
		LoginUserInfo loginUserInfo = super.getUserInfo(request);
		UserInfo userInfo = loginUserInfo.getUserInfo();
		acctLogForm.setUserId(userInfo.getId());// 只能看自己的订单
		if (isFormError(bindingResult, model, acctLogForm)) {
			return;
		}
		if (StringUtils.isNotEmpty(acctLogForm.getId())
				|| StringUtils.isNotEmpty(acctLogForm.getBillId())
				|| StringUtils.isNotEmpty(acctLogForm.getBizOrderId())) {
			acctLogForm.setEndGmtCreate(null);
			acctLogForm.setStartGmtCreate(null);
		} else {
			Date date = null;
			try {
				date = DateTool.strintToDate(DateTool.parseDate(new Date()));
			} catch (ParseException e) {
			}
			if (acctLogForm.getEndGmtCreate() == null) {
				acctLogForm.setEndGmtCreate(date);
			}
			if (acctLogForm.getStartGmtCreate() == null) {
				acctLogForm.setStartGmtCreate(date);
			}
			if (!check2Date(acctLogForm.getStartGmtCreate(), acctLogForm.getEndGmtCreate())) {
				super.alertErrorNoneBack(model, "订单时间区间不能超过2个月");
			}
		}
		AcctLogQuery acctLogQuery = formToAcctLogQuery(acctLogForm);
		Result<List<AcctLog>> result = queryService.queryAcctLog(acctLogQuery);
		acctLogForm.setTotalItem(acctLogQuery.getTotalItem());
		model.addAttribute("acctLogList", result.getModule());
	}

	private AcctLogQuery formToAcctLogQuery(AcctLogForm acctLogForm) {
		AcctLogQuery acctLogQuery = new AcctLogQuery();
		if (StringUtils.isNotEmpty(acctLogForm.getId())) {
			acctLogQuery.setId(Long.parseLong(acctLogForm.getId()));
		}
		if (StringUtils.isNotEmpty(acctLogForm.getBizOrderId())) {
			acctLogQuery.setBizOrderId(Long.parseLong(acctLogForm.getBizOrderId()));
		}
		if (acctLogForm.getStartGmtCreate() != null) {
			acctLogQuery.setStartGmtCreate(acctLogForm.getStartGmtCreate());
		}
		if (acctLogForm.getEndGmtCreate() != null) {
			acctLogQuery.setEndGmtCreate(acctLogForm.getEndGmtCreate());
		}
		acctLogQuery.setUserId(acctLogForm.getUserId());
		if (StringUtils.isNotEmpty(acctLogForm.getBillId())) {
			acctLogQuery.setBillId(Long.parseLong(acctLogForm.getBillId()));
		}
		if (acctLogForm.getTradeType() != null) {
			acctLogQuery.setTradeType(acctLogForm.getTradeType());
		}
		if (acctLogForm.getBillType() != null) {
			acctLogQuery.setBillType(acctLogForm.getBillType());
		}
		if (acctLogForm.getPageSize() > com.longan.web.utils.Constants.PageSize.LARGE) {
			acctLogQuery.setPageSize(com.longan.web.utils.Constants.PageSize.LARGE);// pageSize超过最大时设置为pageSize=最大
			acctLogForm.setPageSize(com.longan.web.utils.Constants.PageSize.LARGE);// 前端显示为最大
		} else {
			acctLogQuery.setPageSize(acctLogForm.getPageSize());
		}
		if (acctLogForm.getCurrentPage() != null) {
			acctLogQuery.setCurrentPage(acctLogForm.getCurrentPage());
		}
		return acctLogQuery;
	}

	@ModelAttribute("billTypeList")
	public Map<String, String> billTypeList() {
		Map<String, String> mapBill = new HashMap<String, String>(3);
		mapBill.put(Constants.AcctLog.BILL_TYPE_CHARGE_ORDER + "",
				Constants.AcctLog.BILL_TYPE_CHARGE_ORDER_DESC);
		mapBill.put(Constants.AcctLog.BILL_TYPE_PAY_ORDER + "",
				Constants.AcctLog.BILL_TYPE_PAY_ORDER_DESC);
		mapBill.put(Constants.AcctLog.BILL_TYPE_REFUND_ORDER + "",
				Constants.AcctLog.BILL_TYPE_REFUND_ORDER_DESC);
		mapBill.put(Constants.AcctLog.BILL_TYPE_CASH_ORDER + "",
				Constants.AcctLog.BILL_TYPE_CASH_ORDER_DESC);
		return mapBill;
	}

	@ModelAttribute("tradeTypeList")
	public Map<String, String> tradeTypeList() {
		Map<String, String> mapTrade = new HashMap<String, String>(2);
		mapTrade.put(Constants.AcctLog.TRADE_TYPE_IN + "", Constants.AcctLog.TRADE_TYPE_IN_DESC);
		mapTrade.put(Constants.AcctLog.TRADE_TYPE_OUT + "", Constants.AcctLog.TRADE_TYPE_OUT_DESC);
		return mapTrade;
	}

	@ModelAttribute("pageSizeList")
	public Map<Integer, Integer> pageSizeList() {
		Map<Integer, Integer> map = new LinkedHashMap<Integer, Integer>();
		map.put(com.longan.web.utils.Constants.PageSize.SMALL,
				com.longan.web.utils.Constants.PageSize.SMALL);
		map.put(com.longan.web.utils.Constants.PageSize.MIDDLE,
				com.longan.web.utils.Constants.PageSize.MIDDLE);
		map.put(com.longan.web.utils.Constants.PageSize.LARGE,
				com.longan.web.utils.Constants.PageSize.LARGE);
		return map;
	}
}
