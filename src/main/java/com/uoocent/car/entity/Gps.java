package com.uoocent.car.entity;

import java.io.Serializable;
@SuppressWarnings("serial")
public class Gps implements Serializable{
	/**
	 * obd设备编号
	 */
	private String device;
	/**
	 * 发送时间
	 */
	private String senddate;
	/**
	 * GPS纬度
	 */
	private Double latitude;
	/**
	 * GPS经度
	 */
	private Double longitude;

	/**
	 * 油位高度
	 */
	private Double fuel_level;

    /**
     * 正反转
     */
    private int turn_state;

    public int getTurn_state() {
        return turn_state;
    }

    public void setTurn_state(int turn_state) {
        this.turn_state = turn_state;
    }

    public Double getFuel_level() {
		return fuel_level;
	}

	public void setFuel_level(Double fuel_level) {
		this.fuel_level = fuel_level;
	}

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
