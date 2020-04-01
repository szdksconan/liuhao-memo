package com.liuhao.rabbitmq;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;

/**
 * 测试用
 */
public class ConnectionUtil {
    /**
     * 建立与RabbitMQ的连接
     */
    public static Connection getConnection() throws Exception {
        //定义连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //设置服务地址
        factory.setHost("127.0.0.1");
        //端口
        factory.setPort(5672);
        //设置账号信息，用户名、密码、vhost
        factory.setVirtualHost("/liuhao");
        factory.setUsername("liuhao");
        factory.setPassword("liuhao");
        // 通过工程获取连接
        Connection connection = factory.newConnection();
        return connection;
    }

}
