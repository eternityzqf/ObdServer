package com.uoocent.car.daoM;

import java.util.List;
import java.util.Map;

import com.uoocent.car.entity.Car;

public interface CarDao {
	
	public Car findBy(Car car);
	
	public Car findByMap(Map<String,Object> map);
	
	public List<Car> listByMap(Map<String,Object> map);
	
	public void add(Car car);
	
	public void update(Car car);
	
	public List<Map<String,String>> listVehicle(Map<String,Object> map);
	
	public int listVehicleCount(Map<String,Object> map);
	
	public Car onlyValidation(Map<String,Object> map);
	
	public Map<String,String> getCarInfo(Map<String,String> map);
	
	public Map<String,Object> getCarLocation(Map<String,String> map);
	
}

