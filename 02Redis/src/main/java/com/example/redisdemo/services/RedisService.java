package com.example.redisdemo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class RedisService {
    @Autowired
    RedisTemplate redisTemplate;

    public void test(){
        ValueOperations ops = redisTemplate.opsForValue();
        Object date = new Date();
        ops.set("k1",date);
        System.out.println("Redis存入数据：" + date);
        Object obj = ops.get("k1");
        System.out.println("Redis查询数据：" + obj);
    }
}
