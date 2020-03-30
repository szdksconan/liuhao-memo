package com.liuhao.lemo.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class kafkaProducerDemo {


    public void send(){
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9093,localhost:9094,localhost:9095");
        props.put(ProducerConfig.ACKS_CONFIG, "1");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        final KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(props);
        // 发送100W条数据
        long start = System.currentTimeMillis();
        for (int i = 0; i < 5000000; i++) {
            kafkaProducer.send(new ProducerRecord<>("my-topic", i + ""));
        }
        long end  = System.currentTimeMillis();
        long time = end-start;
        System.out.println("生产500W数据消耗时间："+time);



        try {
            Thread.sleep(10000l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    public static void main(String[] args) {
        kafkaProducerDemo kafkaProducerDemo = new kafkaProducerDemo();
        kafkaProducerDemo.send();
    }



}
