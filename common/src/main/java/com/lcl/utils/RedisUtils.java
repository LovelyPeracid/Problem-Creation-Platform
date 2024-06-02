package com.lcl.utils;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author LovelyPeracid
 */

@Component
@RequiredArgsConstructor
public class RedisUtils {
    //@Autowired
    private final StringRedisTemplate stringRedisTemplate;

    public  boolean  tryLock(String key){
        Boolean absent = stringRedisTemplate.opsForValue().setIfAbsent(key, "1", 10, TimeUnit.SECONDS);
        return BooleanUtils.isTrue(absent);
    }
    public  void unlock(String key){
        Boolean delete = stringRedisTemplate.delete(key);
    }
    public void setWithLogicExpire(){
        return;
    }
}
