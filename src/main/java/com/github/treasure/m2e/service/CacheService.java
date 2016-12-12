package com.github.treasure.m2e.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

/**
 * Mem service
 */
@Service
public class CacheService implements InitializingBean{

    private Map<String,List<Object>> cache;

    /**
     * 向cache添加数据
     * 
     */
    public void saveList(String key,Object value){
	List<Object> list = cache.get(key);
	if(list == null){
	    list = new ArrayList<Object>();
	    list.add(value);
	    cache.put(key, list);
	}
	list.add(value);
    }
    
    /**
     * 从cache批量取数据
     * 
     */
    public List<Object> getList(String key){
	List<Object> list = null;
	synchronized(cache){
	    list = cache.get(key);
	    cache.remove(key);
	}
	return list;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
	if(cache == null){
	    cache = new HashMap<String,List<Object>>();
	}
    }
}
