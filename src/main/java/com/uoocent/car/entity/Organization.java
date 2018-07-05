package com.uoocent.car.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.uoocent.car.entity.Organization;
@SuppressWarnings("serial")
public class Organization implements Serializable{
	/*
	 * 机构id
	 */
	private Long id;
	/*
	 * 机构名称
	 */
	private String name;
	/*
	 * 父级id
	 */
	private Long parent;
	/*
	 * 机构数据状态
	 */
	private String status;
	
	/*
	 * 是否展开         插件扩展属性
	 */
	private Boolean expanded= false;
	/*
	 * 所在层级         插件扩展属性
	 */
	private String level;
	/*
	 * 子级机构         插件扩展属性
	 */
	private List<Organization> children = new ArrayList<Organization>();
	
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
	public Long getParent() {
		return parent;
	}
	public void setParent(Long parent) {
		this.parent = parent;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	public Boolean getExpanded() {
		return expanded;
	}
	public void setExpanded(Boolean expanded) {
		this.expanded = expanded;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public List<Organization> getChildren() {
		return children;
	}
	public void setChildren(List<Organization> children) {
		this.children = children;
	}
	public void addChildren(Organization children) {
		this.children.add(children);
	}
	
}
