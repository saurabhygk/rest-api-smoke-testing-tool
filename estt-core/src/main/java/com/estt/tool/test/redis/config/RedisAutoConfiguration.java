package com.estt.tool.test.redis.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import redis.clients.jedis.JedisCluster;

@Configuration
public class RedisAutoConfiguration {

	@Value("${redis.host}")
	private String redisHost;

	@Value("${redis.port}")
	private int redisPort;

	/**
	 * This method creates the {@link RedisTemplate} bean to call Redis Operations
	 * 
	 * @param <T>               Any Type of object returned in response
	 * @param connectionFactory {@link RedisConnectionFactory} class which has
	 *                          connection detail on basis of properties file for
	 *                          Redis
	 * @return {@link RedisTemplate} created bean
	 */
	@Bean("defaultRedisTemplate")
	public <T> RedisTemplate<String, T> redisTemplate(RedisConnectionFactory connectionFactory) {
		JedisConnectionFactory jedisConnectionFactory = jedisConnectionFactory();
		if (null == jedisConnectionFactory) {
			return null;
		}
		final RedisTemplate<String, T> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(jedisConnectionFactory());
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
		return redisTemplate;
	}

	/**
	 * Create the {@link JedisCluster} bean
	 * 
	 * @return {@link JedisCluster} created bean
	 */
	@Bean
	JedisConnectionFactory jedisConnectionFactory() {
		// TODO: currently hard coded host and port wiil be read from properties
		try {
			RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHost, redisPort);
			return new JedisConnectionFactory(config);
		} catch (Exception ex) {
			return null;
		}
	}

}