package com.liuhao.auto.demo1;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ApplicationRunTest {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(AnnotationBeanFactoryConfig.class);
        TestA testA = (TestA) ctx.getBean("testA");

    }
}
