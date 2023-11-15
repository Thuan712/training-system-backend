package com.thinkvitals.cache;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Set;

@Repository
public class RedisStoreRepository {
    private ValueOperations valueOperations;
    private RedisTemplate redisTemplate;

    public RedisStoreRepository(RedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
        this.valueOperations = this.redisTemplate.opsForValue();
        this.redisTemplate.setKeySerializer(new StringRedisSerializer());
    }

    public void save(String key, Object value){
        this.valueOperations.set(key, value);
    }

    public void save(String key, Object value, Duration duration) {
        this.valueOperations.set(key, value, duration);
    }

    public void remove(Object key) {
        this.redisTemplate.delete(key);
    }

    public Object get(String key) {
        return this.valueOperations.get(key);
    }

    public Set<String> getAllKeys(Object pattern) {
        return this.redisTemplate.keys(pattern);
    }
}
