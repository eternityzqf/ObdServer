package com.uoocent.car.daoM;

import java.util.List;
import java.util.Map;

import com.uoocent.car.entity.Dictionary;

public interface DictionaryDao{
	
	List<Dictionary> findtextAndValue(Map<String,Object> map);
}
