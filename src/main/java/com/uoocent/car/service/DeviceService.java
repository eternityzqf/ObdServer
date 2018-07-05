package com.uoocent.car.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.uoocent.car.entity.Car;
import com.uoocent.car.entity.Device;

public interface DeviceService{
	
	Device findByODevice(String device);
	
	Device findByMap(Map<String,Object> map);
	
	List<Device> listByMap(Map<String,Object> map);
	
	void addDeviceAndCar(Device device, Car car);
	
	JSONObject getObdData(String device, String exchange);
	
	int countByODevice(Map<String,String> map);
	
	void updateDispatcher(Map<String,String> map,Boolean isAdd);

	void updateCarAddDevice(Device device, Car car, Device nDevice, Car nCar);
	
	void updateDeviceAddCar(Device device, Car car);
	
	Device onlyValidation(Map<String,Object> map);
	
	JSONObject getDeviceInfoData(String device);

	void removeCarAndDevice(String deviceCode, Long carId);

	Map<String, String> checkDeviceO(String device);
	
}
