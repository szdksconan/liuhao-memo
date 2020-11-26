/*
package com.liuhao.memo.mq.service;

import com.liuhao.memo.mq.po.OrderEntity;
import com.liuhao.memo.mq.vo.OrderSubmitVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class OrderService {

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Transactional(rollbackFor = Throwable.class)
    public void createOrder(OrderSubmitVO orderSubmitVO){

        // 获取orderToken
        String orderToken = orderSubmitVO.getOrderToken();

        // 1. 防重复提交，查询redis中有没有orderToken信息，有，则是第一次提交，放行并删除redis中的orderToken
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        DefaultRedisScript<Long> defaultRedisScript  = new DefaultRedisScript<>();
        List<String> keyList = new ArrayList();
        keyList.add("ORDER:TOKEN:"+orderToken);

        defaultRedisScript.setResultType(Long.class);
        defaultRedisScript.setScriptText(script);
        Long flag = redisTemplate.execute(defaultRedisScript,keyList,orderToken);
        if (flag == 0) {
            throw new RuntimeException("订单不可重复提交！");
        }
        //扣减库存


        //生成订单
        OrderEntity order = new OrderEntity();

    }


}
*/
