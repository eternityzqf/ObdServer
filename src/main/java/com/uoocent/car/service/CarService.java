package com.uoocent.car.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.uoocent.car.entity.Car;

public interface CarService{
	
	public Car findBy(Car car);
	
	public Car findByMap(Map<String,Object> map);
	
	public List<Car> listByMap(Map<String,Object> map);
	
	public JSONObject listVehiclePage(Map<String,Object> map);
	
	public Car onlyValidation(Map<String,Object> map);
	
	public Map<String,String> getCarInfo(Map<String,String> map);
	
	public Map<String,Object> getCarLocation(Map<String,String> map);
}
