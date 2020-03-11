package com.liuhao.lemo.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 多数据源测试
 */
@RequestMapping("redis")
@Component
public class RedisDataSourceController {

    @Autowired
    @Qualifier("defRedisTemplate")
    private RedisTemplate<String, Object> delRedisTemplate;

    @Autowired
    @Qualifier("orderRedisTemplate")
    private RedisTemplate<String, Object> orderRedisTemplate;

    @Autowired
    @Qualifier("customerRedisTemplate")
    private RedisTemplate<String, Object> customerRedisTemplate;



    @RequestMapping("/getSomeDatsSourceData")
    public void getSomeDatsSourceData(){
        System.out.println(delRedisTemplate.opsForValue().get("hello"));
        System.out.println(orderRedisTemplate.opsForValue().get("liqian"));
        System.out.println(customerRedisTemplate.opsForHash().values("liuhao"));
    }



}
