package com.longan.mng.action.biz;

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

import com.longan.biz.cached.LocalCachedService;
import com.longan.biz.core.ExcelExportService;
import com.longan.biz.core.OperationLogService;
import com.longan.biz.dataobject.OperationLog;
import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.dataobject.SupplyOrderQuery;
import com.longan.biz.dataobject.UserInfo;
import com.longan.biz.domain.Result;
import com.longan.biz.utils.Constants;
import com.longan.biz.utils.OperLogUtils;
import com.longan.mng.action.common.BaseController;

@Controller
@RequestMapping("biz/supplyOrderExport")
public class SupplyOrderExport extends BaseController {
	@Resource
	private ExcelExportService excelExportService;

	@Resource
	private LocalCachedService localCachedService;

	@Resource
	private OperationLogService operationLogService;

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView onRequest(
			@ModelAttribute("supplyOrderQuery") SupplyOrderQuery supplyOrderQuery,
			HttpSession session) {
		// 业务 权限判断
		UserInfo userInfo = super.getUserInfo(session);
		// 业务权限判断
		if (supplyOrderQuery.getBizId() != null) {
			if (!checkBizAuth(supplyOrderQuery.getBizId(), userInfo)) {
				return new ModelAndView("error/autherror");
			}
		}

		if (userInfo.isDownStreamUser()) {
			supplyOrderQuery.setUserId(userInfo.getId());
		}

		logger.warn("操作员:" + userInfo.getUserName() + "执行供货单导出操作。");
		Result<List<SupplyOrder>> result = excelExportService
				.querySupplyOrderExport(supplyOrderQuery);
		Map<String, Object> model = new HashMap<String, Object>();
		if (!result.isSuccess()) {
			logger.error("SupplyOrderExport error msg : " + result.getResultMsg());
			model.put("errorMsg", result.getResultMsg());
			return new ModelAndView("error/error", model);
		}
		model.put("supplyOrderList", result.getModule());
		String fileName = "供货单导出";
		if (supplyOrderQuery.getBizId() != null) {
			fileName = Constants.BIZ_MAP.get(supplyOrderQuery.getBizId()) + fileName;
		}
		model.put("fileName", fileName);
		ModelAndView modelAndView = new ModelAndView("supplyOrderExcel", model);

		@SuppressWarnings("unchecked")
		Map<String, String> map = (HashMap<String, String>) session.getAttribute("requestInfoMap");
		OperationLog operationLog = OperLogUtils.operationLogDeal(null, null, userInfo,
				map.get("moduleName"), supplyOrderQuery.getBizId(), map.get("loginIp"));
		operationLogService.createOperationLog(operationLog);
		return modelAndView;
	}
}
