package com.liuhao.redis;

import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RedisClient {


    private Jedis jedis;//非切片 连接
    private JedisPool jedisPool;//非切片 连接池
    private ShardedJedis shardedJedis;// 切片 连接
    private ShardedJedisPool shardedJedisPool;//切片 连接池



    public RedisClient()
    {
        initPool();
        initShardedPool();
        shardedJedis = shardedJedisPool.getResource();
        jedis = jedisPool.getResource();


    }

    private void initPool(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(10);
        config.setMaxTotal(40);
        config.setMaxWaitMillis(1000l);
        config.setTestOnBorrow(false);
        jedisPool = new JedisPool(config,"127.0.0.1",6379);
    }


    private void initShardedPool(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(10);
        config.setMaxTotal(40);
        config.setMaxWaitMillis(1000l);
        config.setTestOnBorrow(false);
        // slave链接
        List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
        shards.add(new JedisShardInfo("127.0.0.1", 6379, "master"));
        // 构造池
        shardedJedisPool = new ShardedJedisPool(config, shards);
    }



    private void keyOperate(){

        System.out.println("=================keyOperate===============");
        System.out.println("==========="+jedis.flushDB());
        System.out.println("====判断key999是否存在"+shardedJedis.exists("key999"));
        System.out.println("====新增key001,value001键值对："+shardedJedis.set("key001","value001"));
        System.out.println("====判断key001是否存在："+shardedJedis.exists("key001"));
        // 输出系统中所有的key
        System.out.println("新增key002,value002键值对："+shardedJedis.set("key002", "value002"));

        Set<String> keys = jedis.keys("*");
        keys.forEach(key-> System.out.println(key));



    }








}
