package com.liuhao.spring.ioc;

import org.springframework.beans.factory.annotation.Autowired;


public class IocInstanceA {

    @Autowired
    private IocInstanceB iocInstanceB;

    public IocInstanceA() {
        System.out.println("A 的 构造方法");
    }

    public void get(){
        System.out.println("A get something");
    }

    public IocInstanceB getIocInstanceB() {
        return iocInstanceB;
    }

    public void setIocInstanceB(IocInstanceB iocInstanceB) {
        this.iocInstanceB = iocInstanceB;
    }
}
