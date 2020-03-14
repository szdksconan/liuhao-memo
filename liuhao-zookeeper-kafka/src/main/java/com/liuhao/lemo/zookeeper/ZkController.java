package com.liuhao.lemo.zookeeper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("zk")
@Component
public class ZkController {

    @Autowired
    DistributeLock distributeLock;

    @RequestMapping("/lock")
    public void zkLock() throws Exception {
        distributeLock.lock("/customer");

    }




}
