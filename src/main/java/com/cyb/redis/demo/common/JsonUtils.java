package com.cyb.redis.demo.common;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Json序列化工具类型
 * 
 * @author Administrator
 *
 */
public final class JsonUtils {

	/**
	 * 将对象序列化为json字符串
	 * 
	 * @param bean
	 *            对象
	 * @return json字符串
	 */
	public static String bean2Json(Object bean) {
		String json = null;

		ObjectMapper mapper = new ObjectMapper();
		try {
			json = mapper.writeValueAsString(bean);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return json;
	}

	/**
	 * 将json字符串反序列化为对象
	 * 
	 * @param json
	 *            json字符串
	 * @return 对象
	 */
	public static <T> T json2Bean(String json, Class<T> beanType) {
		T bean = null;

		ObjectMapper mapper = new ObjectMapper();
		try {
			bean = mapper.readValue(json, beanType);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return bean;
	}
}
