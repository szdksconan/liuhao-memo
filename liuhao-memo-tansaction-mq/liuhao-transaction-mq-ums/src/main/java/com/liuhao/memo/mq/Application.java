package com.liuhao.memo.mq;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("liuhao.memo.mq.mapper")
public class Application {


	public static void main(String[] args) {

		SpringApplication.run(Application.class, args);
		System.out.println(("----- selectAll method test ------"));

	}

}
