package com.liuhao.spring.ioc;

import org.springframework.beans.factory.annotation.Autowired;

public class IocInstanceB {

    @Autowired
    IocInstanceA iocInstanceA;

    public IocInstanceB() {
        System.out.println("B 的构造方法");
    }

    public void get(){
        System.out.println("B get something");
    }
}
