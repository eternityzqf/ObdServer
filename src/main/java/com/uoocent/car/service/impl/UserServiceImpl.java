package com.uoocent.car.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.uoocent.car.daoM.UserDao;
import com.uoocent.car.entity.User;
import com.uoocent.car.service.UserService;

@Service
public class UserServiceImpl implements UserService{
	@Resource
	private UserDao userDao;

	public User findByMap(Map<String, Object> map) {
		return userDao.findByMap(map);
	}

	public User findBy(User user) {
		return userDao.findBy(user);
	}

	public User findById(Long id) {
		return userDao.findById(id);
	}
	
	
}
