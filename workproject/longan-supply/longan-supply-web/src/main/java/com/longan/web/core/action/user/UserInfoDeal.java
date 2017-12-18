package com.longan.web.core.action.user;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.longan.biz.core.AcctService;
import com.longan.biz.core.UserService;
import com.longan.biz.dataobject.AcctInfo;
import com.longan.biz.dataobject.UserInfo;
import com.longan.biz.domain.Result;
import com.longan.web.biz.domain.LoginUserInfo;
import com.longan.web.common.controller.BaseController;
import com.longan.web.common.secure.CSRFTokenManager;
import com.longan.web.core.form.ModifyPayPwdForm;
import com.longan.web.core.form.ModifyPwdForm;
import com.longan.web.core.form.UserInfoForm;

@Controller
public class UserInfoDeal extends BaseController {
	@Resource
	private AcctService acctService;

	@Resource
	private UserService userService;

	@Resource
	private Validator validator;

	@Resource
	private CSRFTokenManager csrfTokenManager;

	@RequestMapping("user/userInfoShow")
	public void userInfoShow(HttpServletRequest request, Model model) {
		LoginUserInfo loginUserInfo = super.getUserInfo(request);
		UserInfo userInfo = loginUserInfo.getUserInfo();
		Result<UserInfo> relult = userService.getUserInfo(userInfo.getId());
		UserInfo user = relult.getModule();
		model.addAttribute("user", user);// userInfo已经用过了，这里用user
		// 账户信息
		if (userInfo.getAcctId() != null) {
			Result<AcctInfo> acctInfoResult = acctService.getAcctInfo(userInfo.getAcctId());
			model.addAttribute("acctInfo", acctInfoResult.getModule());
		}
	}

	@RequestMapping(value = "user/userInfoEdit", method = RequestMethod.GET)
	public void userInfoEditIndex(HttpServletRequest request, Model model) {
		LoginUserInfo loginUserInfo = super.getUserInfo(request);
		UserInfo userInfo = loginUserInfo.getUserInfo();
		Result<UserInfo> relult = userService.getUserInfo(userInfo.getId());
		UserInfo user = relult.getModule();
		model.addAttribute("user", user);
	}

	@RequestMapping(value = "user/userInfoEdit", method = RequestMethod.POST)
	public String userInfoEdit(@ModelAttribute("userInfoForm") UserInfoForm userInfoForm,
			BindingResult bindingResult, HttpServletRequest request, Model model) {
		LoginUserInfo loginUserInfo = super.getUserInfo(request);
		UserInfo userInfo = loginUserInfo.getUserInfo();
		model.addAttribute("user", userInfo);
		validator.validate(userInfoForm, bindingResult);
		if (bindingResult.hasErrors()) {
			model.addAttribute("errorList", bindingResult.getAllErrors());
			return "user/userInfoEdit";
		}

		if (!csrfTokenManager.checkToken(request, userInfo.getId())) {
			List<ObjectError> allErrors = new ArrayList<ObjectError>();
			allErrors.add(new FieldError("indexForm", "errorMsg", "你的操作已超时或者重复提交"));
			model.addAttribute("errorList", allErrors);
			return "user/userInfoEdit";
		}

		logger.warn("用户：" + userInfo.getLoginId() + "  进行了商户信息修改操作!");
		UserInfo newUserInfo = formToUserInfo(userInfo, userInfoForm);
		Result<Boolean> result = userService.updateUserInfo(newUserInfo);
		if (result.isSuccess()) {
			model.addAttribute("operName", "userInfoEdit");
			model.addAttribute("resultDescription", "修改成功!");
			model.addAttribute("result", result);
			return "user/operResult";
		} else {
			List<ObjectError> allErrors = new ArrayList<ObjectError>();
			allErrors.add(new FieldError("indexForm", "errorMsg", result.getResultMsg()));
			model.addAttribute("errorList", allErrors);
			return "user/userInfoEdit";
		}
	}

	private UserInfo formToUserInfo(UserInfo userInfo, UserInfoForm userInfoForm) {
		userInfo.setCompayInfo(userInfoForm.getCompayInfo());
		userInfo.setEmail(userInfoForm.getEmail());
		userInfo.setUserName(userInfoForm.getUserName());
		return userInfo;
	}

	@RequestMapping(value = "user/loginPwdEdit", method = RequestMethod.GET)
	public void loginPwdEditIndex(HttpServletRequest request, Model model) {
	}

	@RequestMapping(value = "user/loginPwdEdit", method = RequestMethod.POST)
	public String loginPwdEdit(@ModelAttribute("modifyPwdForm") ModifyPwdForm modifyPwdForm,
			BindingResult bindingResult, HttpServletRequest request, Model model) {
		validator.validate(modifyPwdForm, bindingResult);
		if (bindingResult.hasErrors()) {
			model.addAttribute("errorList", bindingResult.getAllErrors());
			return "user/loginPwdEdit";
		}
		LoginUserInfo loginUserInfo = super.getUserInfo(request);
		UserInfo user = loginUserInfo.getUserInfo();
		if (!csrfTokenManager.checkToken(request, user.getId())) {
			List<ObjectError> allErrors = new ArrayList<ObjectError>();
			allErrors.add(new FieldError("indexForm", "errorMsg", "你的操作已超时或者重复提交"));
			model.addAttribute("errorList", allErrors);
			return "user/loginPwdEdit";
		}
		logger.warn("用户：" + user.getLoginId() + "  进行了登录密码修改操作!");
		Result<Boolean> result = userService.modifyPwd(user.getId(), modifyPwdForm.getOldPwd(),
				modifyPwdForm.getNewPwd());

		if (result.isSuccess()) {
			model.addAttribute("operName", "loginPwdEdit");
			model.addAttribute("resultDescription", "登录密码修改成功!");
			model.addAttribute("result", result);
			return "user/operResult";
		} else {
			List<ObjectError> allErrors = new ArrayList<ObjectError>();
			allErrors.add(new FieldError("indexForm", "errorMsg", result.getResultMsg()));
			model.addAttribute("errorList", allErrors);
			return "user/loginPwdEdit";
		}
	}

	@RequestMapping(value = "user/payPwdEdit", method = RequestMethod.GET)
	public void payPwdEditIndex(HttpServletRequest request, Model model) {
		hasSetPayPwd(request, model);
	}

	@RequestMapping(value = "user/payPwdEdit", method = RequestMethod.POST)
	public String payPwdEdit(@ModelAttribute("modifyPayPwdForm") ModifyPayPwdForm modifyPayPwdForm,
			BindingResult bindingResult, HttpServletRequest request, Model model) {
		validator.validate(modifyPayPwdForm, bindingResult);
		if (bindingResult.hasErrors()) {
			model.addAttribute("errorList", bindingResult.getAllErrors());
			return "user/payPwdEdit";
		}
		LoginUserInfo loginUserInfo = super.getUserInfo(request);
		UserInfo user = loginUserInfo.getUserInfo();
		if (!csrfTokenManager.checkToken(request, user.getId())) {
			List<ObjectError> allErrors = new ArrayList<ObjectError>();
			allErrors.add(new FieldError("indexForm", "errorMsg", "你的操作已超时或者重复提交"));
			model.addAttribute("errorList", allErrors);
			model.addAttribute("isSetPayPwd", "true");
			return "user/payPwdEdit";
		}
		logger.warn("用户：" + user.getLoginId() + "  进行了支付密码修改操作!");
		Result<Boolean> result = userService.modifyPayPwd(user.getId(),
				modifyPayPwdForm.getOldPayPwd(), modifyPayPwdForm.getNewPayPwd());
		if (result.isSuccess()) {
			model.addAttribute("operName", "payPwdEdit");
			model.addAttribute("resultDescription", "支付密码修改成功!");
			model.addAttribute("result", result);
			return "user/operResult";
		} else {
			List<ObjectError> allErrors = new ArrayList<ObjectError>();
			allErrors.add(new FieldError("indexForm", "errorMsg", result.getResultMsg()));
			model.addAttribute("errorList", allErrors);
			model.addAttribute("isSetPayPwd", "true");
			return "user/payPwdEdit";
		}
	}

	@RequestMapping(value = "user/payPwdSet", method = RequestMethod.POST)
	public String payPwdSet(@ModelAttribute("modifyPayPwdForm") ModifyPayPwdForm modifyPayPwdForm,
			HttpServletRequest request, Model model) {
		if (StringUtils.isEmpty(modifyPayPwdForm.getPayPwd())) {
			List<ObjectError> allErrors = new ArrayList<ObjectError>();
			allErrors.add(new FieldError("modifyPayPwdForm", "errorMsg", "支付密码不能为空"));
			model.addAttribute("errorList", allErrors);
			return "user/PayPwdEdit";
		}
		LoginUserInfo loginUserInfo = super.getUserInfo(request);
		UserInfo user = loginUserInfo.getUserInfo();
		if (!csrfTokenManager.checkToken(request, user.getId())) {
			List<ObjectError> allErrors = new ArrayList<ObjectError>();
			allErrors.add(new FieldError("modifyPayPwdForm", "errorMsg", "你的操作已超时或者重复提交"));
			model.addAttribute("errorList", allErrors);
			model.addAttribute("isSetPayPwd", "true");
			return "user/PayPwdEdit";
		}

		Result<UserInfo> rt = userService.getUserInfo(user.getId());
		UserInfo oldUser = null;
		if (rt.isSuccess()) {
			oldUser = rt.getModule();
		} else {
			List<ObjectError> allErrors = new ArrayList<ObjectError>();
			allErrors.add(new FieldError("indexForm", "errorMsg", rt.getResultMsg()));
			model.addAttribute("errorList", allErrors);
			model.addAttribute("isSetPayPwd", "false");
			return "user/payPwdEdit";
		}
		if (StringUtils.isNotEmpty(oldUser.getPayPwd())) {
			List<ObjectError> allErrors = new ArrayList<ObjectError>();
			allErrors.add(new FieldError("modifyPayPwdForm", "errorMsg", "你之前已设置支付密码"));
			model.addAttribute("errorList", allErrors);
			model.addAttribute("isSetPayPwd", "true");
			return "user/PayPwdEdit";
		}

		logger.warn("用户：" + user.getLoginId() + "  进行了支付密码设置操作!");
		Result<Boolean> result = userService.setPayPwd(user.getId(), modifyPayPwdForm.getPayPwd());

		if (result.isSuccess()) {
			model.addAttribute("operName", "payPwdSet");
			model.addAttribute("resultDescription", "支付密码设置成功!");
			model.addAttribute("result", result);
			return "user/operResult";
		} else {
			List<ObjectError> allErrors = new ArrayList<ObjectError>();
			allErrors.add(new FieldError("indexForm", "errorMsg", result.getResultMsg()));
			model.addAttribute("errorList", allErrors);
			model.addAttribute("isSetPayPwd", "false");
			return "user/payPwdEdit";
		}
	}
}
