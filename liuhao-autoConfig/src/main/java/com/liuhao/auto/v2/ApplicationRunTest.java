package com.liuhao.auto.v2;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ApplicationRunTest {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(TestConfig.class);
       // ctx.getBean("com.liuhao.auto.v2.TestA");
    }
}
