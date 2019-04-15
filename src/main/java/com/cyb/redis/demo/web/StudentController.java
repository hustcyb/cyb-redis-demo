package com.cyb.redis.demo.web;

import java.util.List;
import java.util.Map;

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

import com.cyb.redis.demo.domain.Student;
import com.cyb.redis.demo.service.StudentService;
import com.cyb.redis.demo.util.JsonUtils;

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
	 * 获取学生列表
	 * 
	 * @return 学生列表
	 */
	@GetMapping
	public List<Student> listStudents() {
		if (logger.isDebugEnabled()) {
			logger.debug("StudentController.listStudents: start");
		}

		List<Student> students = studentService.listStudents();
		if (logger.isDebugEnabled()) {
			logger.debug("StudentController.listStudents: end, return = {}",
					JsonUtils.bean2Json(students));
		}

		return students;
	}

	/**
	 * 获取最新的学生列表
	 * 
	 * @return 最新的学生列表
	 */
	@GetMapping("new")
	public List<Student> listNewStudents() {
		if (logger.isDebugEnabled()) {
			logger.debug("StudentController.listNewStudents: start");
		}

		List<Student> newStudents = studentService.listNewStudents();
		if (logger.isDebugEnabled()) {
			logger.debug("StudentController.listNewStudents: end, return = {}",
					JsonUtils.bean2Json(newStudents));
		}

		return newStudents;
	}

	/**
	 * 获取学生成绩排名
	 * 
	 * @return 学生成绩排名
	 */
	@GetMapping("top")
	public List<Student> listTopStudents() {
		if (logger.isDebugEnabled()) {
			logger.debug("StudentController.listTopStudents: start");
		}

		List<Student> students = studentService.listTopStudents();
		if (logger.isDebugEnabled()) {
			logger.debug("StudentController.listTopStudents: end, return = {}",
					JsonUtils.bean2Json(students));
		}

		return students;
	}

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
	 * 获取学生统计字典
	 * 
	 * @return 学生统计字典
	 */
	@GetMapping("stats")
	public Map<String, Long> listStudentStats() {
		if (logger.isDebugEnabled()) {
			logger.debug("StudentController.listStudentStats: start");
		}
		
		Map<String, Long> stats = studentService.listStudentStats();
		if (logger.isDebugEnabled()) {
			logger.debug("StudentController.listStudentStats: end, return = {}");
		}
		
		return stats;
	}

	/**
	 * 获取学生人数
	 * 
	 * @return 学生人数
	 */
	@GetMapping("stats/student")
	public Long getStudentCount() {
		if (logger.isDebugEnabled()) {
			logger.debug("StudentController.getStudentCount: start");
		}
		
		Long studentCount = studentService.getStudentCount();
		if (logger.isDebugEnabled()) {
			logger.debug("StudentController.getStudentCount: end, return = {}", studentCount);
		}
		
		return studentCount;
	}

	/**
	 * 获取优秀学生人数
	 * 
	 * @return 优秀学生人数
	 */
	@GetMapping("stats/excellent")
	public Long getExcellentCount() {
		if (logger.isDebugEnabled()) {
			logger.debug("StudentController.getExcellentCount: start");
		}
		
		Long excellentCount = studentService.getExcellentCount();
		if (logger.isDebugEnabled()) {
			logger.debug("StudentController.getExcellentCount: end, return = {}", excellentCount);
		}
		
		return excellentCount;
	}

	/**
	 * 保存学生
	 * 
	 * @param student
	 *            学生
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
