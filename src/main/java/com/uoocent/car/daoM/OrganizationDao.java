package com.uoocent.car.daoM;

import java.util.List;
import java.util.Map;

import com.uoocent.car.entity.Organization;

public interface OrganizationDao{
	public Organization findById(Long id);
	
	public List<Organization> listByMap(Map<String,Object> map);
}
