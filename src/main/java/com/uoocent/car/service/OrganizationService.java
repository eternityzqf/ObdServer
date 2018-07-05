package com.uoocent.car.service;

import java.util.List;

import com.uoocent.car.entity.Organization;

public interface OrganizationService{
	
	public List<Organization> getOrganizationTree(Long uid);
	
}
