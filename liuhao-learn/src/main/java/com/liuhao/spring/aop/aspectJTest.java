package com.liuhao.spring.aop;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class aspectJTest {


    @Before(value = "execution(* com.liuhao.spring.aop.*.*(..))")
    public void doBefore(){
        System.out.println("before ");
    }

    @After(value = "execution(* com.liuhao.spring.aop.*.*(..))")
    public void after(){
        System.out.println("before ");
    }
}
