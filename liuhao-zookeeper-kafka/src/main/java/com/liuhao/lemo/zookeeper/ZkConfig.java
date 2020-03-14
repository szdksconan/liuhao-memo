package com.liuhao.lemo.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class ZkConfig {

    @Value("${zk.url}")
    private String zkUrl;

    @Bean
    public CuratorFramework getCuratorFramework(){
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,3);
        CuratorFramework client = CuratorFrameworkFactory.newClient(zkUrl,retryPolicy);
        client.start();
        return client;
    }

   /* public static void main(String[] args) throws Exception {

        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,3);
        CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181",retryPolicy);
        client.start();

        //获取所有节点
        List<String> childrens = client.getChildren().forPath("/");
        childrens.forEach(s-> System.out.println(s));

        if(client.checkExists().forPath("/lock") == null) {
            client.create().creatingParentsIfNeeded().forPath("/lock");//创建一个look节点
        }

        client.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                .forPath("/lock/");
        client.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                .forPath("/lock/");
        client.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                .forPath("/lock/");

        System.out.println("11");
        System.out.println("11");
        System.out.println("11");


    }
*/
}
