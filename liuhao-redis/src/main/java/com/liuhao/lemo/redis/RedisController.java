package com.liuhao.lemo.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Random;


@Component
@RequestMapping("redis")
public class RedisController {


    @Autowired
    DistributeLock distributeLock;

    Random testRandom = new Random();

    @RequestMapping(value = "/trylock")
    public void tryLock(){
        int ranIndex = testRandom.nextInt(3);
        try {
            boolean getLock = distributeLock.tryLock(distributeLock.getKeys().get(ranIndex)
                    ,distributeLock.getValue().get(ranIndex));
        if(!getLock){
            return;
        }
        //业务处理
        Thread.sleep(1000l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            distributeLock.unLock(distributeLock.getKeys().get(ranIndex),distributeLock.getValue().get(ranIndex));
        }
    }

}
