package com.liuhao.lemo.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Random;


@Component
@RequestMapping("redis")
public class RedisController {


    @Autowired
    DistributeLock distributeLock;

    Random testRandom = new Random();

    @RequestMapping(value = "/tryLock",method = RequestMethod.GET)
    public String tryLock(){
        int ranIndex = testRandom.nextInt(3);//测试数据
        boolean getLock = false;
        try {
             getLock = distributeLock.tryLock(distributeLock.getKeys().get(ranIndex)
                    ,distributeLock.getValue().get(ranIndex));
        if(!getLock){
            return "";
        }
        //业务处理
        Thread.sleep(1000l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if(getLock) {
                distributeLock.unLock(distributeLock.getKeys().get(ranIndex), distributeLock.getValue().get(ranIndex));
            }
        }
        return "";
    }

}
