package com.uoocent.car.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.uoocent.car.entity.Dictionary;
import com.uoocent.car.service.DictionaryService;
import com.uoocent.car.util.ResponseCode;
import com.uoocent.car.util.Result;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "dictionary", description = "数据字典")
@Controller
public class DictionaryController {
	private static final Logger logger = Logger.getLogger(OrganizationController.class);
	@Resource
	private DictionaryService dictionaryService;

 	@ApiOperation(value = "获取数据字典列表", httpMethod = "GET", response = Result.class, notes = "必须的参数：{parentId:数据字典父级id}")
	@RequestMapping(value="app/obd/dictionary", method = RequestMethod.GET)
	@ResponseBody
	public Result getDictionaryList(String parentId,HttpServletRequest request){
 		try {
			//参数验证
			Result res = Result.error(ResponseCode.missing_parameter.getMsg());
			if(StringUtils.isBlank(parentId)){
				return res;
			}
 			
 			Map<String, Object> map = new HashMap<String, Object>();
 			map.put("parent_id", parentId);
 			List<Dictionary> list = dictionaryService.findtextAndValue(map);
 			return Result.success(list);
 		} catch (Exception e) {
			logger.error("app/obd/dictionary", e);
			return Result.error(e.getMessage());
 		}
 	}
}
