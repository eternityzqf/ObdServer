package com.uoocent.car.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class User implements Serializable {
	private Long id;        //用户id
	private String name;    //登录用户名
	private String password;//登录密码
	private Long org;       //所属机构
	private String status;  //用户状态
	private Long role;      //所属角色
	private String salt;    //盐
	private String app;     //是否可以登录App
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Long getOrg() {
		return org;
	}
	public void setOrg(Long org) {
		this.org = org;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Long getRole() {
		return role;
	}
	public void setRole(Long role) {
		this.role = role;
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public String getApp() {
		return app;
	}
	public void setApp(String app) {
		this.app = app;
	}
}
