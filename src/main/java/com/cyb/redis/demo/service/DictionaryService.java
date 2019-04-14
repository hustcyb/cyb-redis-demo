package com.cyb.redis.demo.service;

import java.util.Collection;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 字典服务
 * 
 * @author Administrator
 *
 */
@Service
public class DictionaryService {
	
	@Resource
	private StringRedisTemplate stringRedisTemplate;

	/**
	 * 哈希操作集合
	 */
	@Resource(name = "stringRedisTemplate")
	private HashOperations<String, String, String> hashOperations;
	
	public Map<String, String> get(String key) {
		return hashOperations.entries(key);
	}
	
	public String get(String key, String field) {
		return hashOperations.get(key, field);
	}
	
	public Collection<String> get(String key, Collection<String> fields) {
		return hashOperations.multiGet(key, fields);
	}
	
	public void set(String key, String field, String value) {
		hashOperations.put(key, field, value);
	}
	
	public void set(String key, Map<String, String> entries) {
		hashOperations.putAll(key, entries);;
	}
	
	public void remove(String key, String field) {
		hashOperations.delete(key, field);
	}
	
	public void remove(String key, Collection<String> fields) {
		hashOperations.delete(key, fields);
	}
	
	public void remove(String key) {
		stringRedisTemplate.delete(key); 
	}
}
