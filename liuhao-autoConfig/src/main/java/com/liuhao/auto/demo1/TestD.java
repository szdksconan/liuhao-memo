package com.liuhao.auto.demo1;

import org.springframework.stereotype.Component;

@Component
public class TestD {

    public TestD(){
        System.out.println("无参构造Testd");
    }

    public TestD(int i){
        System.out.println("构造函数testD有int参数"+i);
    }

    public TestD(String s){
        System.out.println("构造函数testD有String参数"+s);
    }
}



