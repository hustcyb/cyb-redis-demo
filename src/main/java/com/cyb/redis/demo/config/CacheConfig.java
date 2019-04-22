package com.cyb.redis.demo.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
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
		GenericToStringSerializer<Long> longSerializer = new GenericToStringSerializer<Long>(
				Long.class);
		redisTemplate.setHashValueSerializer(longSerializer);

		return redisTemplate;
	}

	/**
	 * 创建缓存键前缀生成程序
	 * 
	 * @return 缓存键前缀生成程序
	 */
	@Bean
	public CacheKeyPrefix cacheKeyPrefix() {
		return cacheName -> cacheName + ":";
	}

	/**
	 * 创建默认的redis缓存管理程序
	 * 
	 * @param connectionFactory
	 *            redis连接工厂
	 * @return 默认的redis缓存管理程序
	 */
	@Primary
	@Bean
	public RedisCacheManager cacheManager(
			final RedisConnectionFactory connectionFactory) {
		final RedisCacheWriter redisCacheWriter = RedisCacheWriter
				.lockingRedisCacheWriter(connectionFactory);
		final RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration
				.defaultCacheConfig()
				.entryTtl(Duration.ofSeconds(120))
				.computePrefixWith(cacheKeyPrefix())
				.serializeValuesWith(
						SerializationPair
								.fromSerializer(new StringRedisSerializer()));
		return new RedisCacheManager(redisCacheWriter, cacheConfiguration);
	}

	/**
	 * 创建redis学生缓存管理程序
	 * 
	 * @param connectionFactory
	 *            redis连接工厂
	 * @return redis学生缓存管理程序
	 */
	@Bean(name = "studentCacheManager")
	public RedisCacheManager studentCacheManager(
			final RedisConnectionFactory connectionFactory) {
		final RedisCacheWriter redisCacheWriter = RedisCacheWriter
				.lockingRedisCacheWriter(connectionFactory);
		final RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration
				.defaultCacheConfig().entryTtl(Duration.ofSeconds(300))
				.computePrefixWith(cacheKeyPrefix());
		return new RedisCacheManager(redisCacheWriter, cacheConfiguration);
	}
}
