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

import com.cyb.redis.demo.common.JsonUtils;
import com.cyb.redis.demo.domain.Student;
import com.cyb.redis.demo.service.StudentService;

@RequestMapping("students")
@RestController
public class StudentController {

	/**
	 * 日志接口
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(StudentController.class);

	/**
	 * 学生服务
	 */
	@Resource
	private StudentService studentService;

	/**
	 * 获取学生
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("{id}")
	public Student getStudent(@PathVariable Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("StudentController.getStudent: start, id = {}", id);
		}

		Student student = studentService.getStudent(id);
		if (logger.isDebugEnabled()) {
			logger.debug("Studentcontroller.getStudent: end, return = {}",
					JsonUtils.bean2Json(student));
		}

		return student;
	}

	/**
	 * 保存学生
	 * 
	 * @param student 学生
	 */
	@PostMapping
	public void saveStudent(@RequestBody Student student) {
		if (logger.isDebugEnabled()) {
			logger.debug("StudentController.saveStudent: start, student = {}",
					JsonUtils.bean2Json(student));
		}

		studentService.saveStudent(student);
		if (logger.isDebugEnabled()) {
			logger.debug("StudentController.saveStudent: end");
		}
	}

	/**
	 * 删除学生
	 * 
	 * @param id
	 *            学生编号
	 */
	@DeleteMapping("{id}")
	public void deleteStudent(@PathVariable Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("StudentController.deleteStudent: start, id = {}", id);
		}

		studentService.deleteStudent(id);
		if (logger.isDebugEnabled()) {
			logger.debug("StudentController.deleteStudent: end");
		}
	}
}
