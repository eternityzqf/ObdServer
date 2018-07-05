package com.uoocent.car.daoO;

import java.util.List;
import java.util.Map;

import com.uoocent.car.entity.Device;

public interface ObdDeviceDao{
	
	Device findByDevice(String device);
	
	void add(Map<String,String> map);
	
	void update(Map<String,String> map);
	
	int countByDevice(Map<String,String> map);
	
	Map<String,String> getDeviceSet(String device);
	
	List<Map<String,String>> getDeviceGpsInfo(String device);
	
	List<Map<String,String>> getDeviceCarNoticeInfo(String device);
	
	List<Map<String,String>> getDeviceVolHeightAndTrun(String device);

	void removeDevice(String device);
	
}
