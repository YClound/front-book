package com.longan.web.common.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.longan.biz.cached.LocalCachedService;
import com.longan.biz.core.OperationLogService;
import com.longan.biz.core.TaskService;
import com.longan.biz.core.UserService;
import com.longan.biz.dataobject.AreaInfo;
import com.longan.biz.dataobject.Item;
import com.longan.biz.dataobject.UserInfo;
import com.longan.biz.domain.Result;
import com.longan.biz.utils.DateTool;
import com.longan.web.biz.domain.LoginUserInfo;
import com.longan.web.common.secure.CSRFTokenManager;
import com.longan.web.utils.Constants;
import com.longan.web.utils.StringEscapeEditor;

public class BaseController {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	protected LocalCachedService localCachedService;

	@Resource
	protected TaskService taskService;

	@Resource
	protected OperationLogService operationLogService;

	@Resource
	protected Validator validator;

	@Resource
	protected CSRFTokenManager csrfTokenManager;

	@Resource
	protected UserService userService;

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

	public void alertMsgRedirect(Model model, String msg, String url) {
		model.addAttribute("errorMsg", "<script language='javascript'>alert('" + msg
				+ "');window.location.href='" + url + "'</script>");
	}

	public void alertSuccess(Model model) {
		model.addAttribute("errorMsg",
				"<script language='javascript'>alert('操作成功');history.go(-1)</script>");
	}

	public void alertSuccessNoneBack(Model model) {
		model.addAttribute("errorMsg", "<script language='javascript'>alert('操作成功');</script>");
	}

	public void alertSuccess(Model model, Integer backtime) {
		model.addAttribute("errorMsg", "<script language='javascript'>alert('操作成功');history.go("
				+ backtime + ")</script>");
	}

	public void alertSuccess(Model model, String url) {
		model.addAttribute("errorMsg",
				"<script language='javascript'>alert('操作成功');window.location.href='" + url
						+ "'</script>");
	}

	public LoginUserInfo getUserInfo(HttpServletRequest request) {
		Object object = request.getAttribute(Constants.SESSION_USER);
		return (object == null || !(object instanceof LoginUserInfo)) ? null
				: (LoginUserInfo) object;
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

	public String getCookieValue(String cookieName, HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (null != cookies) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie c = cookies[i];
				if (null != c.getName() && c.getName().equals(cookieName)) {
					return c.getValue();
				}
			}
		}
		return null;
	}

	public void addCookie(String cookieKey, String cookieValue, HttpServletResponse response) {
		Cookie cookie = new Cookie(cookieKey, cookieValue);
		cookie.setPath("/");
		response.addCookie(cookie);
	}

	public void removeCookie(String cookieName, HttpServletRequest request,
			HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		if (null != cookies) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie c = cookies[i];
				if (null != c.getName() && c.getName().equals(cookieName)) {
					c.setMaxAge(0);
					c.setPath("/");
					response.addCookie(c);
					break;
				}
			}
		}
	}

	public void clearCookie(HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		if (null != cookies) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie c = cookies[i];
				c.setPath("/");
				c.setMaxAge(0);
				response.addCookie(c);
			}
		}
	}

	public boolean isFormError(BindingResult bindingResult, Model model, Object form) {
		validator.validate(form, bindingResult);
		if (bindingResult.hasErrors()) {
			model.addAttribute("errorList", bindingResult.getAllErrors());
			return true;
		}
		return false;
	}

	public void setErrorMsg(Model model, String errorMsg) {
		model.addAttribute("errorMsg", errorMsg);
	}

	public boolean hasSetPayPwd(HttpServletRequest request, Model model) {
		LoginUserInfo loginUserInfo = getUserInfo(request);
		Result<UserInfo> result = userService.getUserInfo(loginUserInfo.getUserInfo().getId());
		if (!result.isSuccess() || result.getModule() == null) {
			model.addAttribute("errorMsg", result.getResultMsg());
			return false;
		}

		UserInfo user = result.getModule();
		if (StringUtils.isEmpty(user.getPayPwd())) {
			model.addAttribute("isSetPayPwd", false);
			return false;
		}
		model.addAttribute("isSetPayPwd", true);
		return true;
	}
}
