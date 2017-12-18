package com.longan.mng.action.biz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.longan.biz.cached.LocalCachedService;
import com.longan.biz.core.ExcelExportService;
import com.longan.biz.core.OperationLogService;
import com.longan.biz.dataobject.AreaInfo;
import com.longan.biz.dataobject.BizOrder;
import com.longan.biz.dataobject.BizOrderQuery;
import com.longan.biz.dataobject.OperationLog;
import com.longan.biz.dataobject.UserInfo;
import com.longan.biz.domain.Result;
import com.longan.biz.utils.Constants;
import com.longan.biz.utils.OperLogUtils;
import com.longan.mng.action.common.BaseController;

@Controller
@RequestMapping("biz/bizOrderExport")
public class BizOrderExport extends BaseController {
	@Resource
	private ExcelExportService excelExportService;

	@Resource
	private LocalCachedService localCachedService;

	@Resource
	private OperationLogService operationLogService;

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView onRequest(@ModelAttribute("bizOrderQuery") BizOrderQuery bizOrderQuery,
			HttpSession session) {
		// 业务 权限判断
		UserInfo userInfo = super.getUserInfo(session);
		// 业务权限判断
		if (bizOrderQuery.getBizId() != null) {
			if (!checkBizAuth(bizOrderQuery.getBizId(), userInfo)) {
				return new ModelAndView("error/autherror");
			}
		}

		if (userInfo.isDownStreamUser()) {
			bizOrderQuery.setUserId(userInfo.getId());
		}

		logger.warn("操作员:" + userInfo.getUserName() + "执行订单导出操作。");
		Result<List<BizOrder>> result = excelExportService.queryBizOrderExport(bizOrderQuery,
				userInfo.isDownStreamUser());
		Map<String, Object> model = new HashMap<String, Object>();
		if (!result.isSuccess()) {
			logger.error("BizOrderExport error msg : " + result.getResultMsg());
			model.put("errorMsg", result.getResultMsg());
			return new ModelAndView("error/error", model);
		}
		for (BizOrder bizOrder : result.getModule()) {
			if (StringUtils.isNotBlank(bizOrder.getProvinceCode())) {
				AreaInfo areaInfo = localCachedService
						.getProvinceByCode(bizOrder.getProvinceCode());
				if (areaInfo != null) {
					bizOrder.setUidAreaInfo(areaInfo.getProvinceName());
				}
			}
		}
		model.put("bizOrderList", result.getModule());
		String fileName = "订单导出";
		if (bizOrderQuery.getBizId() != null) {
			fileName = Constants.BIZ_MAP.get(bizOrderQuery.getBizId()) + fileName;
		}
		model.put("fileName", fileName);
		model.put("isDownStream", userInfo.isDownStreamUser());
		ModelAndView modelAndView = new ModelAndView("bizOrderExcel", model);

		@SuppressWarnings("unchecked")
		Map<String, String> map = (HashMap<String, String>) session.getAttribute("requestInfoMap");
		OperationLog operationLog = OperLogUtils.operationLogDeal(null, null, userInfo,
				map.get("moduleName"), bizOrderQuery.getBizId(), map.get("loginIp"));
		operationLogService.createOperationLog(operationLog);
		return modelAndView;
	}

}
