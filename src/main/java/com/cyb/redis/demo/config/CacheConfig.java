package com.cyb.redis.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 缓存配置
 * 
 * @author Administrator
 *
 */
@Configuration
public class CacheConfig {

	/**
	 * 初始化Redis模板
	 * 
	 * @param connectionFactory
	 *            Redis连接工厂
	 * @return Redis模板
	 */
	@Bean
	public RedisTemplate<String, ?> redisTemplate(
			RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
		redisTemplate.setConnectionFactory(connectionFactory);
		RedisSerializer<String> stringRedisSerializer = new StringRedisSerializer();
		redisTemplate.setKeySerializer(stringRedisSerializer);

		return redisTemplate;
	}
}
