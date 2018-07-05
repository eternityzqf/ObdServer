package com.uoocent.car.entity;

import java.io.Serializable;
@SuppressWarnings("serial")
public class CarNotice implements Serializable{
	/**
	 * obd设备编号
	 */
	private String device;
	/**
	 * 发送时间
	 */
	private String senddate;
	/**
	 * 报警代码
	 */
	private String alm_id;
	/**
	 * 报警描述
	 */
	private String alm_description;
	/**
	 * GPS纬度
	 */
	private Double latitude;
	/**
	 * GPS经度
	 */
	private Double longitude;
	
	public String getDevice() {
		return device;
	}
	public void setDevice(String device) {
		this.device = device;
	}
	public String getSenddate() {
		if(senddate!=null&&senddate.endsWith(".0"))
			return senddate.substring(0,  senddate.length()-2);
		return senddate;
	}
	public void setSenddate(String senddate) {
		this.senddate = senddate;
	}
	public String getAlm_id() {
		return alm_id;
	}
	public void setAlm_id(String alm_id) {
		this.alm_id = alm_id;
	}
	public String getAlm_description() {
		return alm_description;
	}
	public void setAlm_description(String alm_description) {
		this.alm_description = alm_description;
	}	
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	
}
