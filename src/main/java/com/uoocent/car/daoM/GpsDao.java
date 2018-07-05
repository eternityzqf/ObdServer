package com.uoocent.car.daoM;

import java.util.List;

import com.uoocent.car.entity.Gps;

public interface GpsDao {
	
	public List<Gps> listByDevice(String device);
	
}

