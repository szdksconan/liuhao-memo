package com.liuhao.rabbitmq.spring;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class Comsumer1 {



    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "spring.test.queue", declare = "true"),
            exchange = @Exchange(
                    value = "spring.test.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC
            ),
            key = "*.*"
    ))
    public void resv(String msg){
        System.out.println("接受到了消息: "+msg);
    }


}
