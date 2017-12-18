package com.longan.mng.action.funds;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.longan.biz.core.ExcelExportService;
import com.longan.biz.core.OperationLogService;
import com.longan.biz.dataobject.AcctLog;
import com.longan.biz.dataobject.AcctLogQuery;
import com.longan.biz.dataobject.OperationLog;
import com.longan.biz.dataobject.UserInfo;
import com.longan.biz.domain.Result;
import com.longan.biz.utils.OperLogUtils;
import com.longan.mng.action.common.BaseController;

@Controller
@RequestMapping("funds/acctLogExport")
public class AcctLogExport extends BaseController {
	@Resource
	private ExcelExportService excelExportService;

	@Resource
	private OperationLogService operationLogService;

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView onRequest(@ModelAttribute("acctLogQuery") AcctLogQuery acctLogQuery,
			HttpSession session) {
		UserInfo userInfo = super.getUserInfo(session);
		// 业务权限判断
		if (userInfo.isDownStreamUser()) {
			acctLogQuery.setUserId(userInfo.getId());
		}
		logger.warn("操作员:" + userInfo.getUserName() + "执行账户资金流水导出操作。");
		Result<List<AcctLog>> result = excelExportService.queryAcctLogExport(acctLogQuery,
				userInfo.isDownStreamUser());
		Map<String, Object> model = new HashMap<String, Object>();
		if (!result.isSuccess()) {
			logger.error("AcctLogExport error msg : " + result.getResultMsg());
			model.put("errorMsg", result.getResultMsg());
			return new ModelAndView("error/error", model);
		}
		model.put("acctLogList", result.getModule());
		model.put("fileName", "账户资金流水导出");
		model.put("isDownStream", userInfo.isDownStreamUser());
		ModelAndView modelAndView = new ModelAndView("acctLogExcel", model);

		@SuppressWarnings("unchecked")
		Map<String, String> map = (HashMap<String, String>) session.getAttribute("requestInfoMap");
		OperationLog operationLog = OperLogUtils.operationLogDeal(null, null, userInfo,
				map.get("moduleName"), null, map.get("loginIp"));
		operationLogService.createOperationLog(operationLog);
		return modelAndView;
	}
}
