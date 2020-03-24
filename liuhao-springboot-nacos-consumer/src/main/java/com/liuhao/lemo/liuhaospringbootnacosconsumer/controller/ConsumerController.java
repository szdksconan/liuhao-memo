package com.liuhao.lemo.liuhaospringbootnacosconsumer.controller;

import com.liuhao.lemo.liuhaospringbootnacosconsumer.rpc.ProviderServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsumerController {

    @Value("${name}")
    private String name;

    @Value("${redis.url}")
    private String redisUrl;

    @Autowired
    private ProviderServer providerServer;

    @GetMapping("hello")
    public String hello(){
        return redisUrl+" "+ name +" "+  providerServer.hello();
    }


}