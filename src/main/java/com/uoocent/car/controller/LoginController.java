package com.uoocent.car.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.uoocent.car.entity.User;
import com.uoocent.car.service.UserService;
import com.uoocent.car.util.Constant;
import com.uoocent.car.util.PasswordUtil;
import com.uoocent.car.util.ResponseCode;
import com.uoocent.car.util.Result;

@Api(value = "login", description = "登录")
@Controller
public class LoginController {
	private static final Logger logger = Logger.getLogger(LoginController.class);
	@Resource
	private UserService userService;

	@ApiOperation(value = "obd工具用户登录", httpMethod = "POST", response = Result.class, notes = "登录必须的参数：name,password")
	@RequestMapping(value = "app/obd/login", method = RequestMethod.POST)
	@ResponseBody
	public Result obdLogin(User loginUser, HttpServletRequest request) {
		try {
			Result res = Result.error(ResponseCode.missing_parameter.getMsg());
			if (loginUser == null ) {
				return res;
			}
			String name = loginUser.getName();
			String pass = loginUser.getPassword();
			if (StringUtils.isBlank(name) || StringUtils.isBlank(pass)) {
				return res;
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("status", Constant.ADD_STATUS);
			map.put("name", name);
			User user = userService.findByMap(map);
			//参数错误
			if (user == null) {
				return Result.error(ResponseCode.unknown_account.getMsg());
			}
			//没有app登录权限
			if (Constant.APP.equals(user.getApp())) {
				return Result.error(ResponseCode.forbidden_account.getMsg());
			}
			//密码验证
			String salt = user.getSalt();
			Map<String, String> hmMap = PasswordUtil.encodePasswordWithConstomSalt(pass, salt);
			String pwd = hmMap.get("encodePassword");
			//密码错误
			if (!pwd.equals(user.getPassword())) {
				return Result.error(ResponseCode.password_incorrect.getMsg());
			}
			//登录成功
			return Result.success(user);
		} catch (Exception e) {
			logger.error("app/obd/login", e);
			return Result.error(e.getMessage());
		}
	}
}
