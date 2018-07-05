package com.uoocent.car.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.uoocent.car.daoM.DictionaryDao;
import com.uoocent.car.entity.Dictionary;
import com.uoocent.car.service.DictionaryService;



@Service
public class DictionaryServiceImpl implements DictionaryService {
	
	@Resource
	private DictionaryDao dicDao;

	public List<Dictionary> findtextAndValue(Map<String, Object> map) {
		return dicDao.findtextAndValue(map);
	}

}
