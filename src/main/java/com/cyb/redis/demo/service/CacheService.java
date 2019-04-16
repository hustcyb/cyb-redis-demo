package com.cyb.redis.demo.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.cyb.redis.demo.domain.Student;
import com.cyb.redis.demo.util.JsonUtils;
import com.google.common.collect.Maps;

/**
 * 缓存注解示例服务
 * 
 * @author Administrator
 *
 */
@CacheConfig(cacheNames = "CACHE")
@Service
public class CacheService {
	
	/**
	 * 日志接口
	 */
	private static final Logger logger = LoggerFactory.getLogger(CacheService.class);
	
	/**
	 * 值字典
	 */
	private static Map<String, String> values = Maps.newHashMap();

	/**
	 * 学生列表
	 */
	private static Map<Integer, Student> students = Maps.newHashMap();
	
	static {
		values.put("abc", "1234");
		
		Student student = new Student();
		student.setId(1);
		student.setName("Jim");
		student.setScore((byte)75);		
		students.put(student.getId(), student);
	}
	
	/**
	 * 根据键获取值
	 * 
	 * @param key 键
	 * @return 值
	 */
	@Cacheable(key = "'VALUE:' + #key")
	public String getValue(String key) {
		if (logger.isDebugEnabled()) {
			logger.debug("CacheableService.getValueByKey: start, key = {}", key);
		}
		
		String value = values.get(key);
		if (logger.isDebugEnabled()) {
			logger.debug("CacheableService.getValueByKey: end, return = {}", value);
		}
		
		return value;
	}
	
	@Cacheable(key = "'STUD:' + #id", cacheManager = "studentCacheManager")
	public Student getStudent(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("CacheableService.getById: start, id = {}", id);
		}
		
		Student student = students.get(id);
		if (logger.isDebugEnabled()) {
			logger.debug("CacheableService.getById: end, return = {}", JsonUtils.bean2Json(student));
		}
		
		return student;
	}
}
