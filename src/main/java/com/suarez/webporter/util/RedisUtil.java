package com.suarez.webporter.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


@Component
public class RedisUtil {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void putDataToRedis(String key,Object object){
        redisTemplate.opsForValue().set(key, object);
    }

    public Object putDataToRedis(String key){
        return redisTemplate.opsForValue().get(key);
    }
}
