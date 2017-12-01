package com.cyb.redis.demo.service;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

/**
 * 字符串值服务
 * 
 * @author Administrator
 *
 */
@Service
public class ValueService {

	/**
	 * 日志接口
	 */
	private Logger logger = LoggerFactory.getLogger(ValueService.class);

	/**
	 * Redis字符串类型接口
	 */
	@Resource
	private StringRedisTemplate stringRedisTemplate;

	/**
	 * 字符串值类型操作接口
	 */
	@Resource(name = "stringRedisTemplate")
	private ValueOperations<String, String> valueOperations;

	/**
	 * 获取字符缓存值
	 * 
	 * @param key
	 *            缓存键
	 * @return 缓存值
	 */
	public String getValue(String key) {
		if (logger.isDebugEnabled()) {
			logger.debug("ValueService.getValue: start, key = {}", key);
		}

		String value = valueOperations.get(key);
		if (logger.isDebugEnabled()) {
			logger.debug("ValueService.getValue: end, return = {}", value);
		}

		return value;
	}

	/**
	 * 设置字符串缓存值
	 * 
	 * @param key
	 *            缓存键
	 * @param value
	 *            缓存值
	 */
	public void setValue(String key, String value) {
		if (logger.isDebugEnabled()) {
			logger.debug("ValueService.setValue: start, key = {}, value = {}",
					key, value);
		}

		valueOperations.set(key, value);
		if (logger.isDebugEnabled()) {
			logger.debug("ValueService.setValue: end");
		}
	}
}
