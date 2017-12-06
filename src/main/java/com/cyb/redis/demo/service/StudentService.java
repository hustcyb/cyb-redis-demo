package com.cyb.redis.demo.service;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import com.cyb.redis.demo.common.JsonUtils;
import com.cyb.redis.demo.domain.Student;
import com.google.common.base.Function;
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
	private static final String studentKeyFormat = "students/{0}";

	/**
	 * 学生列表缓存键
	 */
	private static final String studentsKey = "students";

	/**
	 * 新学生缓存键
	 */
	private static final String newStudentsKey = "students/new";

	/**
	 * 学生成绩排行榜缓存键
	 */
	private static final String studentRankKey = "students/rank";

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
	public List<Student> getStudents() {
		if (logger.isDebugEnabled()) {
			logger.debug("StudentService.getStudents: start");
		}

		List<Integer> ids = studentListOperations.range(studentsKey, 0, -1);
		List<String> studentKeys = Lists.transform(ids,
				new Function<Integer, String>() {
					@Override
					public String apply(Integer id) {
						return MessageFormat.format(studentKeyFormat, id);
					}

				});

		List<Student> students = valueOperations.multiGet(studentKeys);
		if (logger.isDebugEnabled()) {
			logger.debug("StudentService.getStudents: end, return = {}",
					JsonUtils.bean2Json(students));
		}

		return students;
	}

	/**
	 * 获取新加入的学生列表
	 * 
	 * @return
	 */
	public List<Student> getNewStudents() {
		if (logger.isDebugEnabled()) {
			logger.debug("StudentService.start: start");
		}

		List<Integer> ids = studentListOperations.range(newStudentsKey, 0, -1);
		List<String> studentKeys = Lists.transform(ids,
				new Function<Integer, String>() {
					@Override
					public String apply(Integer id) {
						return MessageFormat.format(studentKeyFormat, id);
					}
				});

		List<Student> newStudents = valueOperations.multiGet(studentKeys);
		if (logger.isDebugEnabled()) {
			logger.debug("StudentService.getNewStudents: end, return = {}",
					JsonUtils.bean2Json(newStudents));
		}

		return newStudents;
	}

	/**
	 * 获取学生成绩排行榜
	 * 
	 * @return 学生成绩排行榜
	 */
	public List<Student> getStudentRank() {
		if (logger.isDebugEnabled()) {
			logger.debug("StudentService.getStudentRank: start");
		}

		Set<Integer> ids = studentZSetOperations.reverseRange(studentRankKey,
				0, -1);
		Collection<String> studentKeys = Collections2.transform(ids,
				new Function<Integer, String>() {
					@Override
					public String apply(Integer id) {
						return MessageFormat.format(studentKeyFormat, id);
					}
				});

		List<Student> studentRank = valueOperations.multiGet(studentKeys);
		if (logger.isDebugEnabled()) {
			logger.debug("StudentService.getStudentRank: end, return = {}",
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

		String studentKey = MessageFormat.format(studentKeyFormat, id);
		Student student = valueOperations.get(studentKey);
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

		Integer id = student.getId();
		String studentKey = MessageFormat.format(studentKeyFormat, id);
		valueOperations.set(studentKey, student);

		studentListOperations.leftPush("students", id);
		studentListOperations.leftPush("students/new", id);
		studentListOperations.trim(newStudentsKey, 0, 1);

		studentZSetOperations.add(studentRankKey, id, student.getScore());
		studentZSetOperations.removeRange(studentRankKey, 0, -3);

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

		String studentKey = MessageFormat.format(studentKeyFormat, id);
		redisTemplate.delete(studentKey);

		studentListOperations.remove(studentsKey, 0, id);
		studentListOperations.remove(newStudentsKey, 0, id);

		studentZSetOperations.remove(studentRankKey, id);

		if (logger.isDebugEnabled()) {
			logger.debug("StudentService.deleteStudent: end");
		}
	}

}
