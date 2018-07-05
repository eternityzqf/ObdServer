package com.uoocent.car.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.crypto.hash.Hash;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.uoocent.car.daoM.CarDao;
import com.uoocent.car.daoM.CarNoticeDao;
import com.uoocent.car.daoM.DeviceDao;
import com.uoocent.car.daoM.GpsDao;
import com.uoocent.car.daoO.ObdDeviceDao;
import com.uoocent.car.entity.Car;
import com.uoocent.car.entity.CarNotice;
import com.uoocent.car.entity.Device;
import com.uoocent.car.entity.Gps;
import com.uoocent.car.service.DeviceService;
import com.uoocent.car.util.Constant;
import com.uoocent.car.util.DateUtils;



@Service
public class DeviceServiceImpl implements DeviceService{
	@Resource
	private ObdDeviceDao deviceDaoO;
	@Resource
	private DeviceDao deviceDaoM;
	@Resource
	private CarDao carDao;
	@Resource
	private GpsDao gpsDao;
	@Resource
	private CarNoticeDao carNoticeDao;

	public Device findByODevice(String device) {
		return deviceDaoO.findByDevice(device);
	}

	public Map<String, String> checkDeviceO(String device) { return deviceDaoO.getDeviceSet(device); }
	
	public Device findByMap(Map<String, Object> map) {
		return deviceDaoM.findByMap(map);
	}

	public List<Device> listByMap(Map<String, Object> map) {
		return deviceDaoM.listByMap(map);
	}

	public void addDeviceAndCar(Device device, Car car) {
		carDao.add(car);
		Long car_id = car.getId();
		device.setCar_id(car_id);
		deviceDaoM.add(device);
	}

	public JSONObject getObdData(String device,String exchange) {
		JSONObject jsonObject = new JSONObject();
		List<Gps> gps = gpsDao.listByDevice(device);
		jsonObject.put("gps", gps);
		List<CarNotice> carNotice = carNoticeDao.listByDevice(device);
		jsonObject.put("car_notice", carNotice);
		List<Map<String,String>> carFuel = new ArrayList<Map<String, String>>();
		for(int i=0;i<gps.size();i++){
			Map<String, String> tmp = new HashMap<String, String>();
			tmp.put("fuel_high",gps.size() > 0 ? gps.get(0).getFuel_level().toString() : "");
			tmp.put("senddate",gps.size() > 0 ? gps.get(0).getSenddate() : "");
			carFuel.add(tmp);
		}
		jsonObject.put("car_fuel", carFuel);
		if(Constant.EXCHANGE_LPS.equals(exchange) || Constant.EXCHANGE_YXCL.equals(exchange)){
			List<Map<String,String>> carTurn = new ArrayList<Map<String, String>>();
            for(int i=0;i<gps.size();i++){
                Map<String, String> tmp = new HashMap<String, String>();
                tmp.put("is_run",gps.size() > 0 & (gps.get(0).getTurn_state() > 0 &&  gps.get(0).getTurn_state() < 3) ? "1" : "0");
                tmp.put("is_turn",gps.size() > 0 & gps.get(0).getTurn_state() == 1 ? "0" : "1");
                tmp.put("senddate",gps.size() > 0 ? gps.get(0).getSenddate() : "");
                carTurn.add(tmp);
            }
			jsonObject.put("car_turn", carTurn);
		}
		return jsonObject;
	}

	public int countByODevice(Map<String,String> map) {
		return deviceDaoO.countByDevice(map);
	}

	public void updateDispatcher(Map<String,String> map,Boolean isAdd) {
		if(isAdd){
			deviceDaoO.add(map);
		}else{
			deviceDaoO.update(map);
		}
	}

	public void removeCarAndDevice(String deviceCode, Long carId) {
		Device device = new Device();
		device.setDevice(deviceCode);
		device.setStatus("2");
		device.setUpdatedate(DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
		deviceDaoM.update(device);
		if(carId > 0) {
			Car car = new Car();
			car.setId(carId);
			car.setStatus("2");
			car.setUpdatetime(DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
			carDao.update(car);
		}
		deviceDaoO.removeDevice(deviceCode);
	}
	
	public void updateCarAddDevice(Device device, Car car, Device nDevice, Car nCar) {
		if(null != nCar) {
			car.setStatus(Constant.REMOVE_STATUS);
			car.setUpdatetime(DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
			carDao.add(nCar);
		}
		carDao.update(car);
		if(null != nDevice){
			device.setStatus(Constant.REMOVE_STATUS);
			device.setUpdatedate(DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
			if(null != nCar){
				nDevice.setCar_id(nCar.getId());

			}else{
				nDevice.setCar_id(car.getId());
			}
			deviceDaoM.add(nDevice);
		}else{
			if(null != nCar){
				device.setCar_id(nCar.getId());
			}else{
				device.setCar_id(car.getId());
			}
		}
		deviceDaoM.update(device);
	}

	public void updateDeviceAddCar(Device device, Car car) {
		Car upCar = new Car();
		upCar.setId(car.getId());
		upCar.setStatus(Constant.REMOVE_STATUS);
		upCar.setUpdatetime(DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
		carDao.update(upCar);
		car.setId(null);
		carDao.add(car);
		Long car_id = car.getId();
		device.setCar_id(car_id);
		device.setUpdatedate(DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
		deviceDaoM.update(device);
	}

	public Device onlyValidation(Map<String, Object> map) {
		return deviceDaoM.onlyValidation(map);
	}
	
	public JSONObject getDeviceInfoData(String device) {
		JSONObject jsonObject = new JSONObject();
		Map<String,String> deviceSet = deviceDaoO.getDeviceSet(device);
		if(deviceSet != null){
			String exchange = deviceSet.get("exchange");
			if(Constant.EXCHANGE_YXCL.equals(exchange)){
				jsonObject.put("system", Constant.EXCHANGE_YXCL_TEXT);
			} else if(Constant.EXCHANGE_XF.equals(exchange)){
				jsonObject.put("system", Constant.EXCHANGE_XF_TEXT);
			} else if(Constant.EXCHANGE_LPS.equals(exchange)){
				jsonObject.put("system", Constant.EXCHANGE_LPS_TEXT);
			} else {
				jsonObject.put("system", Constant.EXCHANGE_NONE_TEXT);
			};
		}else{
			jsonObject.put("system", Constant.EXCHANGE_NONE_TEXT);
		}
		List<Map<String,String>> gps = deviceDaoO.getDeviceGpsInfo(device);
		jsonObject.put("gps", gps);
		List<Map<String,String>> carNotice = deviceDaoO.getDeviceCarNoticeInfo(device);
		jsonObject.put("car_notice", carNotice);
		List<Map<String,String>> volHeightAndTrun = deviceDaoO.getDeviceVolHeightAndTrun(device);
		jsonObject.put("volheight_trun", volHeightAndTrun);
		return jsonObject;
	}

}
