package com.uoocent.car.daoM;

import java.util.Map;

import com.uoocent.car.entity.User;

public interface UserDao{
	
	public User findByMap(Map<String,Object> map);
	
	public User findBy(User user);
	
	public User findById(Long id);
	
}
