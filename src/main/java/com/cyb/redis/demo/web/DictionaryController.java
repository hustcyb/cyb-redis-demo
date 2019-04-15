package com.cyb.redis.demo.web;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cyb.redis.demo.service.DictionaryService;

@RequestMapping("dictionaries")
@RestController
public class DictionaryController {

	@Autowired
	private DictionaryService dictionaryService;

	/**
	 * 获取指定名称的字典列表
	 * 
	 * @param name
	 *            字典名称
	 * @return 字典
	 */
	@GetMapping("{name}")
	public Map<String, String> get(@PathVariable String name) {
		return dictionaryService.get(name);
	}

	/**
	 * 获取字典字段值
	 * 
	 * @param name
	 *            字典名称
	 * @param field
	 *            字段名称
	 * @return 字段值
	 */
	@GetMapping("{name}/{field:\\w+}")
	public String getValue(@PathVariable String name, @PathVariable String field) {
		return dictionaryService.get(name, field);
	}

	/**
	 * 获取字典字段值列表
	 * 
	 * @param name
	 *            字典名称
	 * @param fields
	 *            字段名称列表
	 * @return 字段值列表
	 */
	@GetMapping("{name}/{fields:\\w+(?:,\\w+)+}")
	public Collection<String> listValues(@PathVariable String name,
			@PathVariable Collection<String> fields) {
		return dictionaryService.get(name, fields);
	}

	/**
	 * 设置字典条目列表
	 * 
	 * @param name
	 *            字典名称
	 * @param entries
	 *            条目列表
	 */
	@PostMapping("{name}")
	public void set(@PathVariable String name,
			@RequestBody Map<String, String> entries) {
		dictionaryService.set(name, entries);
	}

	/**
	 * 设置字典字段
	 * 
	 * @param name
	 *            字典名称
	 * @param field
	 *            字段名称
	 * @param value
	 *            字段值
	 */
	@PostMapping("{name}/{field}")
	public void set(@PathVariable String name, @PathVariable String field,
			@RequestBody String value) {
		dictionaryService.set(name, field, value);
	}

	/**
	 * 删除字典
	 * 
	 * @param name
	 *            字典名称
	 */
	@DeleteMapping("{name}")
	public void delete(@PathVariable String name) {
		dictionaryService.remove(name);
	}

	/**
	 * 删除字典字段
	 * 
	 * @param name
	 *            字典名称
	 * @param field
	 *            字段名称
	 */
	@DeleteMapping("{name}/{field}")
	public void delete(@PathVariable String name, @PathVariable String field) {
		dictionaryService.remove(name, field);
	}
}
