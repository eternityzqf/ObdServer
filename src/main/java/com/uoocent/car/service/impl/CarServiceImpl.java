package com.uoocent.car.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.uoocent.car.daoM.CarDao;
import com.uoocent.car.daoM.DeviceDao;
import com.uoocent.car.daoO.ObdDeviceDao;
import com.uoocent.car.entity.Car;
import com.uoocent.car.service.CarService;

@Service
public class CarServiceImpl implements CarService{
	@Resource
	private CarDao carDao;
	
	@Resource
	private ObdDeviceDao obdDeviceDao;

	public Car findBy(Car car) {
		return carDao.findBy(car);
	}

	public Car findByMap(Map<String, Object> map) {
		return carDao.findByMap(map);
	}

	public List<Car> listByMap(Map<String, Object> map) {
		return carDao.listByMap(map);
	}

	public JSONObject listVehiclePage(Map<String, Object> map) {
		JSONObject jsonObject =  new JSONObject();
		jsonObject.put("total", carDao.listVehicleCount(map));
		jsonObject.put("list", carDao.listVehicle(map));
		return jsonObject;
	}

	public Car onlyValidation(Map<String, Object> map) {
		return carDao.onlyValidation(map);
	}

	public Map<String, String> getCarInfo(Map<String, String> map) {
		Map<String,String> carInfo = carDao.getCarInfo(map);
		if(carInfo != null){
			String extend = carInfo.get("extend");
			if(StringUtils.isNotBlank(extend)){
				String [] extendAry = extend.split(",");
				if(extendAry.length == 3){
					carInfo.put("extend_fuel", extendAry[0]);
					carInfo.put("extend_water", extendAry[1]);
					carInfo.put("extend_trun", extendAry[2]);
				}
			}
		}
		return carInfo;
	}

	public Map<String, Object> getCarLocation(Map<String, String> map) {
		return carDao.getCarLocation(map);
	}

}
