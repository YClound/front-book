package com.longan.web.core.action.biz;

import java.text.ParseException;
import java.util.ArrayList;
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
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.longan.biz.core.QueryService;
import com.longan.biz.dataobject.BizOrder;
import com.longan.biz.dataobject.BizOrderQuery;
import com.longan.biz.dataobject.UserInfo;
import com.longan.biz.domain.Result;
import com.longan.biz.utils.Constants;
import com.longan.biz.utils.DateTool;
import com.longan.web.biz.domain.LoginUserInfo;
import com.longan.web.common.controller.BaseController;
import com.longan.web.core.form.BizOrderForm;

@Controller
@RequestMapping("biz/queryBizOrder")
public class QueryBizOrder extends BaseController {
	@Resource
	private QueryService queryService;
	@Resource
	private Validator validator;

	@RequestMapping(method = RequestMethod.GET)
	public void index(@ModelAttribute("bizOrderQuery") BizOrderForm bizOrderForm,
			HttpServletRequest request, Model model) {
		LoginUserInfo loginUserInfo = super.getUserInfo(request);
		UserInfo userInfo = loginUserInfo.getUserInfo();
		bizOrderForm.setUserId(userInfo.getId());// 只能看自己的订单
		if (StringUtils.isNotEmpty(bizOrderForm.getId())) {
			bizOrderForm.setEndGmtCreate(null);
			bizOrderForm.setStartGmtCreate(null);
		} else {
			Date date = null;
			try {
				date = DateTool.strintToDate(DateTool.parseDate(new Date()));
			} catch (ParseException e) {
			}
			if (bizOrderForm.getEndGmtCreate() == null) {
				bizOrderForm.setEndGmtCreate(date);
			}
			if (bizOrderForm.getStartGmtCreate() == null) {
				bizOrderForm.setStartGmtCreate(date);
			}
			if (!check2Date(bizOrderForm.getStartGmtCreate(), bizOrderForm.getEndGmtCreate())) {
				super.alertErrorNoneBack(model, "订单时间区间不能超过2个月");
			}
		}
		BizOrderQuery bizOrderQuery = formToBizOrderQuery(bizOrderForm);
		Result<List<BizOrder>> result = queryService.queryBizOrderPage(bizOrderQuery);
		bizOrderForm.setTotalItem(bizOrderQuery.getTotalItem());
		model.addAttribute("bizOrderList", result.getModule());
	}

	@RequestMapping(method = RequestMethod.POST)
	public void doQuery(@ModelAttribute("bizOrderQuery") BizOrderForm bizOrderForm,
			BindingResult bindingResult, HttpServletRequest request, Model model) {
		LoginUserInfo loginUserInfo = super.getUserInfo(request);
		UserInfo userInfo = loginUserInfo.getUserInfo();
		bizOrderForm.setUserId(userInfo.getId());// 只能看自己的订单
		if (isFormError(bindingResult, model, bizOrderForm)) {
			return;
		}
		if (StringUtils.isNotEmpty(bizOrderForm.getId())) {
			bizOrderForm.setEndGmtCreate(null);
			bizOrderForm.setStartGmtCreate(null);
		} else {
			Date date = null;
			try {
				date = DateTool.strintToDate(DateTool.parseDate(new Date()));
			} catch (ParseException e) {
			}
			if (bizOrderForm.getEndGmtCreate() == null) {
				bizOrderForm.setEndGmtCreate(date);
			}
			if (bizOrderForm.getStartGmtCreate() == null) {
				bizOrderForm.setStartGmtCreate(date);
			}
			if (!check2Date(bizOrderForm.getStartGmtCreate(), bizOrderForm.getEndGmtCreate())) {
				super.alertErrorNoneBack(model, "订单时间区间不能超过2个月");
			}
		}
		BizOrderQuery bizOrderQuery = formToBizOrderQuery(bizOrderForm);
		Result<List<BizOrder>> result = queryService.queryBizOrderPage(bizOrderQuery);
		bizOrderForm.setTotalItem(bizOrderQuery.getTotalItem());
		model.addAttribute("bizOrderList", result.getModule());
	}

	private BizOrderQuery formToBizOrderQuery(BizOrderForm bizOrderForm) {
		BizOrderQuery bizOrderQuery = new BizOrderQuery();
		if (StringUtils.isNotEmpty(bizOrderForm.getId())) {
			bizOrderQuery.setId(Long.parseLong(bizOrderForm.getId()));
		}
		if (bizOrderForm.getStartGmtCreate() != null) {
			bizOrderQuery.setStartGmtCreate(bizOrderForm.getStartGmtCreate());
		}
		if (bizOrderForm.getEndGmtCreate() != null) {
			bizOrderQuery.setEndGmtCreate(bizOrderForm.getEndGmtCreate());
		}
		bizOrderQuery.setUserId(bizOrderForm.getUserId());
		if (StringUtils.isNotEmpty(bizOrderForm.getItemUid())) {
			bizOrderQuery.setItemUid(bizOrderForm.getItemUid());
		}
		if (bizOrderForm.getBizId() != null) {
			bizOrderQuery.setBizId(bizOrderForm.getBizId());
		}
		if (bizOrderForm.getStatus() != null) {
			if (bizOrderForm.getStatus() == Constants.BizOrder.STATUS_CHARGING) {
				ArrayList<Integer> statusList = new ArrayList<Integer>();
				statusList.add(Constants.BizOrder.STATUS_CHARGING);
				statusList.add(Constants.BizOrder.STATUS_INIT);
				statusList.add(Constants.BizOrder.STATUS_LOCK);
				statusList.add(Constants.BizOrder.STATUS_EXCEPTION);
				statusList.add(Constants.BizOrder.STATUS_UNCONFIRMED);
				bizOrderQuery.setStatusList(statusList);
			} else {
				bizOrderQuery.setStatus(bizOrderForm.getStatus());
			}
		}
		if (bizOrderForm.getPageSize() > com.longan.web.utils.Constants.PageSize.LARGE) {
			bizOrderQuery.setPageSize(com.longan.web.utils.Constants.PageSize.LARGE);// pageSize超过最大时设置为pageSize=最大
			bizOrderForm.setPageSize(com.longan.web.utils.Constants.PageSize.LARGE);// 前端显示为最大
		} else {
			bizOrderQuery.setPageSize(bizOrderForm.getPageSize());
		}
		if (bizOrderForm.getCurrentPage() != null) {
			bizOrderQuery.setCurrentPage(bizOrderForm.getCurrentPage());
		}
		return bizOrderQuery;
	}

	@ModelAttribute("bizList")
	public Map<Integer, String> bizList() {
		return Constants.getBizMap();
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

	@ModelAttribute("statusList")
	public Map<String, String> statusList() {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.BizOrder.STATUS_CHARGING + "", Constants.BizOrder.STATUS_CHARGING_DESC);
		map.put(Constants.BizOrder.STATUS_FAILED + "", Constants.BizOrder.STATUS_FAILED_DESC);
		map.put(Constants.BizOrder.STATUS_SUCCESS + "", Constants.BizOrder.STATUS_SUCCESS_DESC);
		return map;
	}
}
