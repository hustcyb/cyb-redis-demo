package com.cyb.redis.demo.service;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.cyb.redis.demo.common.JsonUtils;
import com.cyb.redis.demo.domain.Student;

/**
 * 学生服务
 * 
 * @author Administrator
 *
 */
@Service
public class StudentService {

	/**
	 * 日志接口
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(StudentService.class);

	/**
	 * Redis学生接口
	 */
	@Resource
	private RedisTemplate<Integer, Student> redisTemplate;

	/**
	 * 学生操作接口
	 */
	@Resource(name = "redisTemplate")
	private ValueOperations<Integer, Student> studentOperations;

	/**
	 * 获取学生
	 * 
	 * @param id
	 *            学生编号
	 * @return 学生
	 */
	public Student getStudent(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("StudentService.getStudent: start, id = {}", id);
		}

		Student student = studentOperations.get(id);
		if (logger.isDebugEnabled()) {
			logger.debug("StudentService.getStudent: end, return = {}",
					JsonUtils.bean2Json(student));
		}

		return student;
	}

	/**
	 * 保存学生
	 * 
	 * @param student
	 *            学生
	 */
	public void saveStudent(Student student) {
		if (logger.isDebugEnabled()) {
			logger.debug("StudentService.saveStudent: start, student = {}",
					JsonUtils.bean2Json(student));
		}

		studentOperations.set(student.getId(), student);
		if (logger.isDebugEnabled()) {
			logger.debug("StudentService.saveStudent: end");
		}
	}

	/**
	 * 删除学生
	 * 
	 * @param student
	 *            学生
	 */
	public void deleteStudent(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("StudentService.deleteStudent: start, id = {}", id);
		}

		redisTemplate.delete(id);
		if (logger.isDebugEnabled()) {
			logger.debug("StudentService.deleteStudent");
		}
	}

}
