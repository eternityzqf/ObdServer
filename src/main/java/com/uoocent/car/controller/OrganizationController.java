package com.uoocent.car.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.uoocent.car.service.OrganizationService;
import com.uoocent.car.service.UserService;
import com.uoocent.car.util.ResponseCode;
import com.uoocent.car.util.Result;
import com.uoocent.car.entity.Organization;

@Api(value = "org", description = "机构")
@Controller
public class OrganizationController {
	private static final Logger logger = Logger.getLogger(OrganizationController.class);
	@Resource
	private OrganizationService organizationService;
	@Resource
	private UserService userService;

 	@ApiOperation(value = "获取机构树", httpMethod = "GET", response = Result.class, notes = "必须的参数：{id:当前登录用户所属机构id}")
	@RequestMapping(value="app/obd/getOrgTree", method = RequestMethod.GET)
	@ResponseBody
	public Result getOrgTree(String uid,HttpServletRequest request) {
 		try {
			Result res = Result.error(ResponseCode.missing_parameter.getMsg());
			if (StringUtils.isBlank(uid)) {
				return res;
			}
 			List<Organization> orgTree = organizationService.getOrganizationTree(Long.parseLong(uid)); 
 			if(orgTree.size() < 1){
 				return Result.error("无法获取机构树");
 			}
 			return Result.success(orgTree);
		} catch (Exception e) {
			logger.error("app/obd/getOrgTree", e);
			return Result.error(e.getMessage());
		}
	}
}
