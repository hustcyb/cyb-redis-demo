package com.cyb.redis.demo.web;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cyb.redis.demo.service.ValueService;

@RequestMapping("/value")
@RestController
public class ValueController {

	/**
	 * 日志接口
	 */
	private Logger logger = LoggerFactory.getLogger(ValueController.class);

	/**
	 * Redis服务
	 */
	@Resource
	private ValueService redisService;

	/**
	 * 获取缓存值
	 * 
	 * @param key
	 *            缓存键
	 * @return 缓存值
	 */
	@GetMapping("{key}")
	public String getValue(@PathVariable String key) {
		if (logger.isDebugEnabled()) {
			logger.debug("ValueController.getValue: start, key = {}", key);
		}

		String value = redisService.getValue(key);
		if (logger.isDebugEnabled()) {
			logger.debug("ValueController.getValue: end, return = {}", value);
		}

		return value;
	}

	/**
	 * 设置缓存值
	 * 
	 * @param key
	 *            缓存键
	 * @param value
	 *            缓存值
	 */
	@PostMapping("{key}")
	public void setValue(@PathVariable String key, @RequestBody String value) {
		if (logger.isDebugEnabled()) {
			logger.debug("ValueController.setValue: start, key = {}, value = {}");
		}
		
		redisService.setValue(key, value);
		if (logger.isDebugEnabled()) {
			logger.debug("ValueController.setValue: end");
		}
	}
}
