package com.liuhao.lemo.liuhaospringbootnacosconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableFeignClients
@RefreshScope
public class LiuhaoSpringbootNacosConsumerApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext =  SpringApplication.run(LiuhaoSpringbootNacosConsumerApplication.class, args);
	}

}
