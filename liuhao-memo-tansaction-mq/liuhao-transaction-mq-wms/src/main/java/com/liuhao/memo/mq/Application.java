package com.liuhao.memo.mq;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@MapperScan("com.liuhao.memo.mq.dao")
@EnableSwagger2
public class Application {


	public static void main(String[] args) {

		SpringApplication.run(Application.class, args);
		System.out.println(("----- selectAll method test ------"));

	}

}
