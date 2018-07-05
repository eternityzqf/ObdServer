package com.uoocent.car.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.uoocent.car.daoM.OrganizationDao;
import com.uoocent.car.daoM.UserDao;
import com.uoocent.car.entity.Organization;
import com.uoocent.car.entity.User;
import com.uoocent.car.service.OrganizationService;
import com.uoocent.car.util.Constant;



@Service
public class OrganizationServiceImpl implements OrganizationService{
	@Resource
	private OrganizationDao organizationDao;
	@Resource
	private UserDao userDao;
	
	public List<Long> getOrganization(List<Long> orgid, Long org) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("parent", org);
		map.put("status", Constant.ADD_STATUS);
		List<Organization> list = organizationDao.listByMap(map);
		if (list.size() > 0) {
			for (Organization orgz : list) {
				Long id = orgz.getId();
				if (!orgid.contains(id)) {
					orgid.add(id);
				}
				getOrganization(orgid, id);
			}
		}
		return orgid;
	}

	public List<Long> getOrganizationByType(Organization o,List<Long> orgid, Long org) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("parent", org);
		map.put("status", Constant.ADD_STATUS);
		List<Organization> list = organizationDao.listByMap(map);
		if (list.size() > 0) {
			for (Organization orgz : list) {
				Long id = orgz.getId();
				if (!orgid.contains(id)) {
					orgid.add(id);
				}
				orgid.add(org);
				getOrganization(orgid, id);
			}
		}
		return orgid;
	}
	
	public List<Long> getOrganizationUser(Organization o ,Long org) {
		List<Long> list = new ArrayList<Long>();
		return getOrganizationByType(o,list, org);
	}
	
	public List<Organization> getOrganization(List<Organization> orgs,Long orgId,int level){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("parent", orgId);
		map.put("status", Constant.ADD_STATUS);
		List<Organization> list = organizationDao.listByMap(map);
		if(list.size() > 0){
			level++;
			for (Organization org : list) {
				org.setLevel(String.valueOf(level));
				if(!orgs.contains(org)){
					orgs.add(org);
				}
				getOrganization(orgs,org.getId(),level);
			}
		}
		return orgs;
	}
	
	public List<Organization> getOrganizationUser(Organization userOrg){
		List <Organization> list = new ArrayList<Organization>();
		int level = 1;
		userOrg.setLevel(String.valueOf(level));
		list.add(userOrg);
		return getOrganization(list,userOrg.getId(),level);
	}

	public List<Organization> getOrganizationTree(Long uid) {
		List<Organization> nodes = new ArrayList<Organization>();
		User user = userDao.findById(uid);
		if(user == null){
			return nodes;
		}
		Long uOrgId = user.getOrg();
		Organization userOrg = organizationDao.findById(uOrgId);
		
		List<Organization> list = getOrganizationUser(userOrg);//获取登录用户机构或机构，及下属所有机构或部门 列表
		Map<Long, Organization> perMap = new HashMap<Long, Organization>();
		for (Organization org : list) {
			if (org.getId() != null && org.getId().equals(uOrgId)) {//添加根级机构，并设置展开
				org.setExpanded(true);
				nodes.add(org);
			}
			perMap.put(org.getId(), org);
			Long parentId = org.getParent();
			if (parentId != null && perMap.containsKey(parentId)) {
				perMap.get(parentId).addChildren(org);
			}
		}
		
		return nodes;
	}
}
