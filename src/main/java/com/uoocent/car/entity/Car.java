package com.uoocent.car.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Car implements Serializable{
	private Long id;
	private String car_no;
	private String car_no_color;
	private String self_num;
	private String brand;
	private String type;
	private String fule_type;
	private Double tank_vol;
	private Double tank_length;
	private Double tank_height;
	private Double tank_width;
	private String driver_image_1;
	private String driver_image_2;
	private String vin;
	private Long mileage;
	private String texts;
	private String obd;
	private Long org;
	private String status;
	private String car_status;
	private Long createdby;
	private String createtime;
	private String updatetime;
	private String device;
	private String device_tpye;
	
	public Long getId(){
	    return id;
	}
	public void setId(Long id){
	    this.id = id;
	}
	public String getCar_no(){
	    return car_no;
	}
	public void setCar_no(String car_no){
	    this.car_no = car_no;
	}
	public String getCar_no_color() {
		return car_no_color;
	}
	public void setCar_no_color(String car_no_color) {
		this.car_no_color = car_no_color;
	}
	public String getSelf_num() {
		return self_num;
	}
	public void setSelf_num(String self_num) {
		this.self_num = self_num;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFule_type() {
		return fule_type;
	}
	public void setFule_type(String fule_type) {
		this.fule_type = fule_type;
	}
	public Double getTank_vol() {
		return tank_vol;
	}
	public void setTank_vol(Double tank_vol) {
		this.tank_vol = tank_vol;
	}
	public Double getTank_length() {
		return tank_length;
	}
	public void setTank_length(Double tank_length) {
		this.tank_length = tank_length;
	}
	public Double getTank_height() {
		return tank_height;
	}
	public void setTank_height(Double tank_height) {
		this.tank_height = tank_height;
	}
	public Double getTank_width() {
		return tank_width;
	}
	public void setTank_width(Double tank_width) {
		this.tank_width = tank_width;
	}
	public String getDriver_image_1() {
		return driver_image_1;
	}
	public void setDriver_image_1(String driver_image_1) {
		this.driver_image_1 = driver_image_1;
	}
	public String getDriver_image_2() {
		return driver_image_2;
	}
	public void setDriver_image_2(String driver_image_2) {
		this.driver_image_2 = driver_image_2;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public Long getMileage(){
	    return mileage;
	}
	public void setMileage(Long mileage){
	    this.mileage = mileage;
	}
	public Long getOrg(){
	    return org;
	}
	public void setOrg(Long org){
	    this.org = org;
	}
	public String getTexts(){
	    return texts;
	}
	public void setTexts(String texts){
	    this.texts = texts;
	}
	public String getObd(){
	    return obd;
	}
	public void setObd(String obd){
	    this.obd = obd;
	}
	public String getStatus(){
	    return status;
	}
	public void setStatus(String status){
	    this.status = status;
	}
	public String getCar_status() {
		return car_status;
	}
	public void setCar_status(String car_status) {
		this.car_status = car_status;
	}
	public Long getCreatedby(){
	    return createdby;
	}
	public void setCreatedby(Long createdby){
	    this.createdby = createdby;
	}
	public String getCreatetime(){
		if(createtime!=null&&createtime.endsWith(",0"))
			return createtime.substring(0, createtime.length()-2);
	    return createtime;
	}
	public void setCreatetime(String createtime){
	    this.createtime = createtime;
	}
	public String getDevice() {
		return device;
	}
	public void setDevice(String device) {
		this.device = device;
	}
	public String getDevice_tpye() {
		return device_tpye;
	}
	public void setDevice_tpye(String device_tpye) {
		this.device_tpye = device_tpye;
	}
	public String getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}

}
