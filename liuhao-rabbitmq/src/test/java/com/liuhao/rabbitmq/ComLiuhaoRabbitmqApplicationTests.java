package com.liuhao.rabbitmq;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ComLiuhaoRabbitmqApplicationTests {

	@Autowired
	private AmqpTemplate amqpTemplate;


	@Test
	void contextLoads() throws InterruptedException {
		String msg = "this is new msg";
		amqpTemplate.convertAndSend("spring.test.exchange","liuhao.liqian",msg);
		Thread.sleep(5000l);
	}

}
