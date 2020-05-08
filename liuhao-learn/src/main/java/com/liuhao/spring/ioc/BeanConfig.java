package com.liuhao.spring.ioc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public IocInstanceA getIocInstanceA(){
        return new IocInstanceA();
    }

    @Bean
    public IocInstanceB getIocInstanceB(){
        return new IocInstanceB();
    }
}
