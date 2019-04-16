package com.cyb.redis.demo.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
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
		redisTemplate.setHashKeySerializer(stringRedisSerializer);
		GenericToStringSerializer<Long> longSerializer = new GenericToStringSerializer<Long>(Long.class);
		redisTemplate.setHashValueSerializer(longSerializer);

		return redisTemplate;
	}
	
	@Primary
	@Bean
	public RedisCacheManager cacheManager(final RedisConnectionFactory connectionFactory) {
		final RedisCacheWriter redisCacheWriter = RedisCacheWriter.lockingRedisCacheWriter(connectionFactory);
	    final RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(120));
	    final RedisCacheManager redisCacheManager = new RedisCacheManager(redisCacheWriter, cacheConfiguration);
	    return redisCacheManager;
	}
	
	@Bean(name = "studentCacheManager")
	public RedisCacheManager studentCacheManager(final RedisConnectionFactory connectionFactory) {
		final RedisCacheWriter redisCacheWriter = RedisCacheWriter.lockingRedisCacheWriter(connectionFactory);
		final RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(300));
	    final RedisCacheManager redisCacheManager = new RedisCacheManager(redisCacheWriter, cacheConfiguration);
	    return redisCacheManager;	
	}
}
