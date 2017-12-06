package com.cyb.redis.demo.web;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cyb.redis.demo.service.ValueService;

/**
 * 字符串控制器
 * 
 * @author Administrator
 *
 */
@RequestMapping("/values")
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
	private ValueService valueService;

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

		String value = valueService.getValue(key);
		if (logger.isDebugEnabled()) {
			logger.debug("ValueController.getValue: end, return = {}", value);
		}

		return value;
	}

	/**
	 * 保存缓存值
	 * 
	 * @param key
	 *            缓存键
	 * @param value
	 *            缓存值
	 */
	@PostMapping("{key}")
	public void saveValue(@PathVariable String key, @RequestBody String value) {
		if (logger.isDebugEnabled()) {
			logger.debug("ValueController.saveValue: start, key = {}, value = {}", key, value);
		}

		valueService.saveValue(key, value);
		if (logger.isDebugEnabled()) {
			logger.debug("ValueController.saveValue: end");
		}
	}

	/**
	 * 缓存缓存值
	 * 
	 * @param key
	 *            缓存值
	 */
	@DeleteMapping("{key}")
	public void deleteValue(@PathVariable String key) {
		if (logger.isDebugEnabled()) {
			logger.debug("ValueController.deleteValue: start, key = {}", key);
		}

		valueService.deleteValue(key);
		if (logger.isDebugEnabled()) {
			logger.debug("ValueController.deleteValue: end");
		}
	}
}
