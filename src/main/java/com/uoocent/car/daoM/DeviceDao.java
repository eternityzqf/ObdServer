package com.uoocent.car.daoM;

import java.util.List;
import java.util.Map;

import com.uoocent.car.entity.Device;

public interface DeviceDao {
	
	public Device findByMap(Map<String,Object> map);
	
	public List<Device> listByMap(Map<String,Object> map);
	
	public void add(Device device);
	
	public void update(Device device);
	
	public Device onlyValidation(Map<String,Object> map);
	
}

