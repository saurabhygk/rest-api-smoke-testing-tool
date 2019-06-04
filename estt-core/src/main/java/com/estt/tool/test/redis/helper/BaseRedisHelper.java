package com.estt.tool.test.redis.helper;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
public class BaseRedisHelper<T> {

    private RedisTemplate<String, T> redisTemplate;

    private HashOperations<String, Object, T> hashOperation;

    private ValueOperations<String, T> valueOperations;

    /**
     * This constructor constructs the RedisUtil object by referring Redis template to utilize as common functionality
     * 
     * @param redisTemplate
     *            redisTemplate
     */
    public BaseRedisHelper(@Qualifier("defaultRedisTemplate") RedisTemplate<String, T> redisTemplate) {

        this.redisTemplate = redisTemplate;
        this.hashOperation = redisTemplate.opsForHash();
        this.valueOperations = redisTemplate.opsForValue();
    }

    /**
     * This method will be used to put Map against the RedisKey - hash operation for map
     * 
     * @param redisKey
     *            Redis key
     * @param mapKey
     *            map key
     * @param value
     *            value
     */
    public void putMap(String redisKey, Object mapKey, T value) {

        hashOperation.put(redisKey, mapKey, value);
    }

    /**
     * This method will be used to get Map against the RedisKey - hash operation for map
     * 
     * @param redisKey
     *            Redis key
     * @param mapKey
     *            map key
     * @return value
     */
    public T getMapAsSingleEntry(String redisKey, Object mapKey) {

        return hashOperation.get(redisKey, mapKey);
    }

    /**
     * This method will be used to get map for all entries - hash operation
     * 
     * @param redisKey
     *            Redis key
     * @return value map
     */
    public Map<Object, T> getMapAsAll(String redisKey) {

        return hashOperation.entries(redisKey);
    }

    /**
     * This method will be used to put the value against key - value operation
     * 
     * @param redisKey
     *            Redis key
     * @param value
     *            value
     */
    public void putValue(String redisKey, T value) {

        valueOperations.set(redisKey, value);
    }

    /**
     * This method will put the value against key with expiration time - value operation
     * 
     * @param redisKey
     *            Redis key
     * @param value
     *            value
     * @param timeout
     *            timeout
     * @param unit
     *            unit
     */
    public void putValueWithExpireTime(String redisKey, T value, long timeout, TimeUnit unit) {

        valueOperations.set(redisKey, value, timeout, unit);
    }

    /**
     * This method will returns the value against key - value operation
     * 
     * @param redisKey
     *            Redis key
     * @return value
     */
    public T getValue(String redisKey) {

        return valueOperations.get(redisKey);
    }

    /**
     * This method will be called externally to set expiration time on key
     * 
     * @param redisKey
     *            Redis key
     * @param timeout
     *            timeout
     * @param unit
     *            unit
     */
    public void setExpire(String redisKey, long timeout, TimeUnit unit) {

        redisTemplate.expire(redisKey, timeout, unit);
    }

    /**
     * This method will be used to delete key from key store
     * 
     * @param redisKey
     *            key
     * @return Boolean
     */
    public Boolean deleteKey(String redisKey) {

        return redisTemplate.delete(redisKey);
    }
}
