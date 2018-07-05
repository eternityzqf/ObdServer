package com.uoocent.car.daoM;

import java.util.List;
import java.util.Map;

import com.uoocent.car.entity.CarNotice;

public interface CarNoticeDao {
	
	public List<CarNotice> listByDevice(String device);
	
	public List<Map<String,String>> listFuelByDevice(String device);
	
	public List<Map<String,String>> listTurnByDevice(String device);
	
}

