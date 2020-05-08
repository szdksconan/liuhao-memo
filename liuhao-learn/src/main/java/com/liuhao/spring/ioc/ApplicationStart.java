package com.liuhao.spring.ioc;


import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ApplicationStart {


    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(BeanConfig.class);
        IocInstanceA iocInstanceA = (IocInstanceA) applicationContext.getBean(IocInstanceA.class);
        iocInstanceA.getIocInstanceB().get();
    }
}
