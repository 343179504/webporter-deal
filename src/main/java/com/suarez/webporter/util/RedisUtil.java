package com.suarez.webporter.util;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Set;

@Component
public class RedisUtil {
    @Resource
    RedisTemplate<String, String> redisTemplate;




    public String get(String key) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        return redisTemplate.opsForValue().get(key);
    }

    public void set(String key, String value) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
            return;
        }
        redisTemplate.opsForValue().set(key, value);
    }
    public Set<String> keys () {
        return redisTemplate.keys("*");
    }
    public Set<String> keysByPre (String pre) {
        if (StringUtils.isEmpty(pre)) {
            return null;
        }
        return redisTemplate.keys(pre+"*");
    }

    public void removeKey(String key) {
        if (!StringUtils.isEmpty(key)) {
            redisTemplate.delete(key);
        }
    }

    public void removeAll() {
        Set<String> keys = redisTemplate.keys("*");

        redisTemplate.delete(keys);

    }

}
