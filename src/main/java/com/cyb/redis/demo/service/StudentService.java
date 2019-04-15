package com.cyb.redis.demo.service;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import com.cyb.redis.demo.domain.Student;
import com.cyb.redis.demo.util.JsonUtils;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

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
	 * 学生信息缓存键格式
	 */
	@Value("${cache.student.id.key.format:STUD:{0}}")
	private String studentKeyFormat;

	/**
	 * 学生列表缓存键
	 */
	@Value("${cache.student.key:STUD}")
	private String studentsKey;

	/**
	 * 新学生缓存键
	 */
	@Value("${cache.student.new.key:STUD:NEW}")
	private String newStudentsKey;

	/**
	 * 学生成绩排名缓存键
	 */
	@Value("${cache.student.top.key:STUD:TOP}")
	private String topStudentKey;

	/**
	 * 学生统计缓存键
	 */
	@Value("${cache.student.stat.key:STUD:STAT}")
	private String studentStatKey;

	/**
	 * 学生人数字段名称
	 */
	private String studentCountField = "studentCount";

	/**
	 * 优秀学生人数字段名称
	 */
	private String excellentCountField = "excellentCount";

	/**
	 * Redis学生接口
	 */
	@Resource
	private RedisTemplate<String, Student> redisTemplate;

	/**
	 * 学生值操作接口
	 */
	@Resource(name = "redisTemplate")
	private ValueOperations<String, Student> valueOperations;

	/**
	 * 学生计数操作接口
	 */
	@Resource(name = "redisTemplate")
	private HashOperations<String, String, Long> studentHashOperations;

	/**
	 * 学生列表操作接口
	 */
	@Resource(name = "redisTemplate")
	private ListOperations<String, Integer> studentListOperations;

	/**
	 * 学生成绩排行榜接口
	 */
	@Resource(name = "redisTemplate")
	private ZSetOperations<String, Integer> studentZSetOperations;

	/**
	 * 获取学生列表
	 * 
	 * @return 学生列表
	 */
	public List<Student> listStudents() {
		if (logger.isDebugEnabled()) {
			logger.debug("StudentService.listStudents: start");
		}

		List<Integer> ids = studentListOperations.range(studentsKey, 0, -1);
		List<String> studentKeys = Lists.transform(ids, this::getStudentKey);
		List<Student> students = valueOperations.multiGet(studentKeys);
		if (logger.isDebugEnabled()) {
			logger.debug("StudentService.listStudents: end, return = {}",
					JsonUtils.bean2Json(students));
		}

		return students;
	}

	/**
	 * 获取新加入的学生列表
	 * 
	 * @return
	 */
	public List<Student> listNewStudents() {
		if (logger.isDebugEnabled()) {
			logger.debug("StudentService.listNewStudents: start");
		}

		List<Integer> ids = studentListOperations.range(newStudentsKey, 0, -1);
		List<String> studentKeys = Lists.transform(ids, this::getStudentKey);
		List<Student> newStudents = valueOperations.multiGet(studentKeys);
		if (logger.isDebugEnabled()) {
			logger.debug("StudentService.listNewStudents: end, return = {}",
					JsonUtils.bean2Json(newStudents));
		}

		return newStudents;
	}

	/**
	 * 获取学生成绩排名
	 * 
	 * @return 学生成绩排名
	 */
	public List<Student> listTopStudents() {
		if (logger.isDebugEnabled()) {
			logger.debug("StudentService.listTopStudents: start");
		}

		Set<Integer> ids = studentZSetOperations.reverseRange(topStudentKey, 0,
				-1);
		Collection<String> studentKeys = Collections2.transform(ids,
				this::getStudentKey);
		List<Student> studentRank = valueOperations.multiGet(studentKeys);
		if (logger.isDebugEnabled()) {
			logger.debug("StudentService.listTopStudents: end, return = {}",
					JsonUtils.bean2Json(studentRank));
		}

		return studentRank;
	}

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

		String studentKey = getStudentKey(id);
		Student student = valueOperations.get(studentKey);
		if (logger.isDebugEnabled()) {
			logger.debug("StudentService.getStudent: end, return = {}",
					JsonUtils.bean2Json(student));
		}

		return student;
	}

	/**
	 * 获取学生统计
	 * 
	 * @return 学生统计
	 */
	public Map<String, Long> listStudentStats() {
		return studentHashOperations.entries(studentStatKey);
	}

	/**
	 * 获取学生人数
	 * 
	 * @return 学生人数
	 */
	public Long getStudentCount() {
		return studentHashOperations.get(studentStatKey, studentCountField);
	}

	/**
	 * 获取优秀学生人数
	 * 
	 * @return
	 */
	public Long getExcellentCount() {
		return studentHashOperations.get(studentStatKey, excellentCountField);
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

		Integer id = student.getId();
		String studentKey = getStudentKey(id);
		valueOperations.set(studentKey, student);

		studentHashOperations.increment(studentStatKey, studentCountField, 1L);
		if (student.getScore() >= 80) {
			studentHashOperations
					.increment(studentStatKey, excellentCountField, 1L);
		}

		studentListOperations.leftPush(studentsKey, id);
		studentListOperations.leftPush(newStudentsKey, id);
		studentListOperations.trim(newStudentsKey, 0, 1);

		studentZSetOperations.add(topStudentKey, id, student.getScore());
		studentZSetOperations.removeRange(topStudentKey, 0, -3);

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

		Student student = getStudent(id);
		if (student != null) {
			studentHashOperations.increment(studentStatKey, studentCountField, -1);
			if (student.getScore() >= 80) {
				studentHashOperations.increment(studentStatKey, excellentCountField, -1);
			}
		}
		
		String studentKey = getStudentKey(id);
		redisTemplate.delete(studentKey);

		studentListOperations.remove(studentsKey, 0, id);
		studentListOperations.remove(newStudentsKey, 0, id);

		studentZSetOperations.remove(topStudentKey, id);

		if (logger.isDebugEnabled()) {
			logger.debug("StudentService.deleteStudent: end");
		}
	}

	/**
	 * 获取学生缓存键
	 * 
	 * @param id
	 *            学生编号
	 * @return 缓存键
	 */
	private String getStudentKey(Integer id) {
		return MessageFormat.format(studentKeyFormat, id);
	}
}
