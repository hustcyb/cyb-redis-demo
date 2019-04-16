package com.cyb.redis.demo.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cyb.redis.demo.domain.Student;
import com.cyb.redis.demo.service.CacheService;

/**
 * 缓存注解示例控制器
 * 
 * @author Administrator
 *
 */
@RequestMapping("cache")
@RestController
public class CacheController {

	/**
	 * 日志接口
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(CacheController.class);

	/**
	 * 缓存注解示例代码
	 */
	@Autowired
	private CacheService cacheService;

	/**
	 * 根据键获取值
	 * 
	 * @param key
	 *            键
	 * @return 值
	 */
	@GetMapping("values/{key}")
	public String getValue(@PathVariable String key) {
		if (logger.isDebugEnabled()) {
			logger.debug("CacheController.getValue: start, id = {}", key);
		}

		String value = cacheService.getValue(key);
		if (logger.isDebugEnabled()) {
			logger.debug("CacheController.getValue: end, return = {}", value);
		}

		return value;
	}

	/**
	 * 根据编号获取学生
	 * 
	 * @param id
	 *            学生编号
	 * @return 学生
	 */
	@GetMapping("students/{id}")
	public Student getStudentById(@PathVariable Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("CacheController.getStudentById: start, id = {}", id);
		}

		Student student = cacheService.getStudent(id);
		if (logger.isDebugEnabled()) {
			logger.debug("CacheController.getStudentById: end, return = {}",
					student);
		}

		return student;
	}
}
