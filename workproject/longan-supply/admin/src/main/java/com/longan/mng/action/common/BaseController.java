package com.longan.mng.action.common;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.longan.biz.cached.LocalCachedService;
import com.longan.biz.core.OperationLogService;
import com.longan.biz.core.TaskService;
import com.longan.biz.dataobject.AreaInfo;
import com.longan.biz.dataobject.Item;
import com.longan.biz.dataobject.OperationLog;
import com.longan.biz.dataobject.Task;
import com.longan.biz.dataobject.UserInfo;
import com.longan.biz.domain.Result;
import com.longan.biz.utils.DateTool;
import com.longan.biz.utils.Md5Encrypt;
import com.longan.mng.form.BaseTaskForm;
import com.longan.mng.utils.Constants;
import com.longan.mng.utils.StringEscapeEditor;

public class BaseController {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final static String PWD_KEY = "longan_login";
	private final static String DEFAUT_PWD = "Flow123456";
	@Resource
	protected LocalCachedService localCachedService;

	@Resource
	protected TaskService taskService;

	@Resource
	protected OperationLogService operationLogService;

	@InitBinder
	public void formInitBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringEscapeEditor());
	}

	public void alertError(Model model, String errorMsg) {
		model.addAttribute("errorMsg", "<script language='javascript'>alert('" + errorMsg
				+ "');history.go(-1)</script>");
	}

	public void alertErrorNoneBack(Model model, String errorMsg) {
		model.addAttribute("errorMsg", "<script language='javascript'>alert('" + errorMsg
				+ "');</script>");
	}

	public void setError(Model model, String errorMsg) {
		model.addAttribute("errorMsg", errorMsg);
	}

	public void alertMsgRedirect(Model model, String msg, String url) {
		model.addAttribute("errorMsg", "<script language='javascript'>alert('" + msg
				+ "');window.location.href='" + url + "'</script>");
	}

	public void alertSuccess(Model model) {
		model.addAttribute("errorMsg",
				"<script language='javascript'>alert('操作成功');history.go(-1)</script>");
	}

	public void alertSuccess(Model model, Integer backtime) {
		model.addAttribute("errorMsg", "<script language='javascript'>alert('操作成功');history.go("
				+ backtime + ")</script>");
	}

	public void alertSuccessNoneBack(Model model) {
		model.addAttribute("errorMsg", "<script language='javascript'>alert('操作成功');</script>");
	}

	public void alertSuccess(Model model, String url) {
		model.addAttribute("errorMsg",
				"<script language='javascript'>alert('操作成功');window.location.href='" + url
						+ "'</script>");
	}

	public UserInfo getUserInfo(HttpSession session) {
		Object object = session.getAttribute(Constants.SESSION_USER);
		UserInfo userInfo = object == null ? null : (UserInfo) object;
		return userInfo;
	}

	public boolean checkBizAuth(Integer bizId, UserInfo userInfo) {
		boolean result = false;
		if (bizId == null) {
			return result;
		}

		Set<Integer> bizSet = localCachedService.getAuthBizByUserId(userInfo.getId());
		if (bizSet == null || !bizSet.contains(bizId)) {
			return result;
		}

		result = true;
		return result;
	}

	public String getRemoteIp(HttpServletRequest request) {
		String result = request.getHeader("X-Forwarded-For");
		if (StringUtils.isEmpty(result)) {
			result = request.getRemoteAddr();
		}

		return result;

	}

	public boolean check2Date(Date startDate, Date endDate) {
		if (startDate == null || endDate == null) {
			return false;
		}
		Calendar start = Calendar.getInstance();
		start.setTime(startDate);
		Calendar end = Calendar.getInstance();
		end.setTime(endDate);
		int count = DateTool.getDayBetween(start, end);
		return count < 62;
	}

	@SuppressWarnings("unchecked")
	public String getModuleNameFromSession(HttpSession session) {
		Map<String, String> map = (HashMap<String, String>) session.getAttribute("requestInfoMap");
		return map.get("moduleName");
	}

	@SuppressWarnings("unchecked")
	public String getIpFromSession(HttpSession session) {
		Map<String, String> map = (HashMap<String, String>) session.getAttribute("requestInfoMap");
		return map.get("loginIp");
	}

	protected void setItemArea(Item item) {
		if (item.isNationwide()) {
			item.setItemSalesAreaDesc(com.longan.biz.utils.Constants.Item.SALE_TYPE_NATIONWIDE_DESC);
		} else {
			StringBuffer sb = new StringBuffer("");
			for (String areaCode : item.getSalesAreaList()) {
				AreaInfo areaInfo = localCachedService.getProvinceByCode(areaCode);
				if (areaInfo != null) {
					sb.append(areaInfo.getProvinceName()).append("、");
				}
			}
			item.setItemSalesAreaDesc(sb.toString().substring(0, sb.toString().length() - 1));
		}
	}

	public Result<Long> submitTaskForm(BaseTaskForm baseTaskForm, String serviceName,
			String methodName, Serializable object, String className, OperationLog operationLog,
			UserInfo userInfo, Integer bizId) {
		Result<Long> result = new Result<Long>();

		if (baseTaskForm == null || StringUtils.isEmpty(serviceName)
				|| StringUtils.isEmpty(methodName) || object == null || operationLog == null
				|| userInfo == null) {
			result.setResultMsg("参数错误");
			return result;
		}

		Task task = new Task();
		task.setServiceName(serviceName);
		task.setMethodName(methodName);
		task.setNativeObject(object);
		task.setUserId(userInfo.getId());
		task.setUserName(userInfo.getUserName());
		task.setModuleName(operationLog.getModuleName());
		task.setObjectName(className);
		task.setInstanceDesc(operationLog.getDescription());
		task.setBizId(bizId);

		Result<Long> taskResult = null;
		if (baseTaskForm.isRealTimeCommit()) {

			task.setCommitType(com.longan.biz.utils.Constants.Task.COMMIT_TYPE_REAL_TIME);

		} else {
			task.setCommitType(com.longan.biz.utils.Constants.Task.COMMIT_TYPE_TASK);
			if (baseTaskForm.getCommitTime() == null) {
				result.setResultMsg("定时执行，生效时间不能为空");
				return result;
			}
			task.setCommitTime(baseTaskForm.getCommitTime());
		}

		taskResult = taskService.submitTask(task);
		if (!taskResult.isSuccess()) {
			result.setResultMsg(taskResult.getResultMsg());
			return result;
		}

		if (baseTaskForm.isRealTimeCommit()) {
			// 即使生效的 需要记录一下操作日志， 定时任务本身就有日志，就不记录了
			operationLogService.createOperationLog(operationLog);
		}

		result.setSuccess(true);
		result.setModule(task.getId());
		return result;
	}

	private String getPwd(String pwd) {
		String result = Md5Encrypt.md5(pwd + PWD_KEY);
		result = Md5Encrypt.md5(result);
		return result;
	}

	protected String getDefaultPwd() {
		return getPwd(DEFAUT_PWD);
	}

	protected String createPrivateKey() {// 创建密钥
		String show = null;
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
			keyGen.initialize(512);
			// 生成公钥和私钥对
			KeyPair key = keyGen.generateKeyPair();
			// 实例化Signature，用于产生数字签名，指定用RSA和SHA算法
			// 得到私钥
			PrivateKey privateKey = key.getPrivate();
			RSAPrivateCrtKey RSAPrivateKey = (RSAPrivateCrtKey) privateKey;
			BigInteger enter = RSAPrivateKey.getPrimeP();
			show = enter.toString(16);
		} catch (NoSuchAlgorithmException e) {
			logger.error("createPrivateKey error", e);
		}
		return show;
	}

	protected boolean isPhone(Integer bizId) {
		if (bizId == null) {
			return false;
		}
		return bizId == com.longan.biz.utils.Constants.BizInfo.CODE_PHONE_MOBILE
				|| bizId == com.longan.biz.utils.Constants.BizInfo.CODE_PHONE_TELECOM
				|| bizId == com.longan.biz.utils.Constants.BizInfo.CODE_PHONE_UNICOM;
	}

	protected boolean isFlow(Integer bizId) {
		if (bizId == null) {
			return false;
		}
		return bizId == com.longan.biz.utils.Constants.BizInfo.CODE_FLOW_MOBILE
				|| bizId == com.longan.biz.utils.Constants.BizInfo.CODE_FLOW_TELECOM
				|| bizId == com.longan.biz.utils.Constants.BizInfo.CODE_FLOW_UNICOM;
	}
	
	protected String getRefererUrlUnPath(HttpServletRequest request) {
		String url = StringUtils.substringBefore(request.getHeader("Referer"), ".do");
		url =  StringUtils.substringAfter(url, request.getContextPath()+"/");
		return url;
	}

}
