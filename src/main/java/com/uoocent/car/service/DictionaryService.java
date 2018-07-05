package com.uoocent.car.service;

import java.util.List;
import java.util.Map;

import com.uoocent.car.entity.Dictionary;

public interface DictionaryService{
	
	List<Dictionary> findtextAndValue(Map<String,Object> map);

}
