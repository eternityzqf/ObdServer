package com.uoocent.car.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Device implements Serializable{

	private Long id;
	private String car_no;
	private String device;
	private String device_type;
	private String sim;
	private String vin;
	private String connectway;
	private String status;
	private Long car_id;
	private String video_device;
	private String video_sim;

	private Long createdby;
	private String createdate;
	private String updatedate;
	
	private String extend;

	private int cameraCount;
	private int screenCount;
	private int interphoneCount;
	private int warnerCount;

	public int getCameraCount() {
		return cameraCount;
	}

	public void setCameraCount(int cameraCount) {
		this.cameraCount = cameraCount;
	}

	public int getScreenCount() {
		return screenCount;
	}

	public void setScreenCount(int screenCount) {
		this.screenCount = screenCount;
	}

	public int getInterphoneCount() {
		return interphoneCount;
	}

	public void setInterphoneCount(int interphoneCount) {
		this.interphoneCount = interphoneCount;
	}

	public int getWarnerCount() {
		return warnerCount;
	}

	public void setWarnerCount(int warnerCount) {
		this.warnerCount = warnerCount;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCar_no() {
		return car_no;
	}
	public void setCar_no(String car_no) {
		this.car_no = car_no;
	}
	public String getDevice() {
		return device;
	}
	public void setDevice(String device) {
		this.device = device;
	}
	public String getDevice_type() {
		return device_type;
	}
	public void setDevice_type(String device_type) {
		this.device_type = device_type;
	}
	public String getSim() {
		return sim;
	}
	public void setSim(String sim) {
		this.sim = sim;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public String getConnectway() {
		return connectway;
	}
	public void setConnectway(String connectway) {
		this.connectway = connectway;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Long getCar_id() {
		return car_id;
	}
	public void setCar_id(Long car_id) {
		this.car_id = car_id;
	}
	public String getVideo_device() {
		return video_device;
	}
	public void setVideo_device(String video_device) {
		this.video_device = video_device;
	}
	public String getVideo_sim() {
		return video_sim;
	}
	public void setVideo_sim(String video_sim) {
		this.video_sim = video_sim;
	}
	public Long getCreatedby() {
		return createdby;
	}
	public void setCreatedby(Long createdby) {
		this.createdby = createdby;
	}
	public String getCreatedate() {
		if(createdate!=null&&createdate.endsWith(".0"))
			return createdate.substring(0,  createdate.length()-2);
	    return createdate;
	}
	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}
	public String getUpdatedate() {
		return updatedate;
	}
	public void setUpdatedate(String updatedate) {
		this.updatedate = updatedate;
	}
	public String getExtend() {
		return extend;
	}
	public void setExtend(String extend) {
		this.extend = extend;
	}

}
