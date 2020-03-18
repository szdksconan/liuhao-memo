package com.liuhao.auto.demo1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestA {

    @Autowired
    private TestB testB;

    public TestA(){
        System.out.println("实例化了TestA");
    }


    public TestB getTestB() {
        return testB;
    }

    public void setTestB(TestB testB) {
        this.testB = testB;
    }

    public void call(){
        System.out.println("call testA");
        System.out.println("testB="+testB);
    }


}
