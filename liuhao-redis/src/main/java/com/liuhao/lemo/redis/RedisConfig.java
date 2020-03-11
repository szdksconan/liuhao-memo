package com.liuhao.lemo.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String hostName;

    @Value("${spring.redis.port}")
    private Integer port;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.lettuce.pool.max-idle}")
    private Integer maxIdle;

    @Value("${spring.redis.lettuce.pool.min-idle}")
    private Integer minIdle;

    @Value("${spring.redis.lettuce.pool.max-active}")
    private Integer maxActive;

    @Value("${spring.redis.lettuce.pool.max-wait}")
    private Long maxWait;

    @Value("${spring.redis.timeout}")
    private Long timeOut;

    @Value("${spring.redis.lettuce.shutdown-timeout}")
    private Long shutdownTimeOut;

    @Value("${spring.redis.order.database}")
    private Integer orderDatabase;

    @Value("${spring.redis.customer.database}")
    private Integer customerDatabase;

    @Value("${spring.redis.database}")
    private Integer database;


    static final Jackson2JsonRedisSerializer jackson2JsonRedisSerializer =
            getJackson2JsonRedisSerializer();
    /**
     * 配置 Jackson2JsonRedisSerializer 序列化器，在配置 redisTemplate需要用来做k,v的
     * 序列化器
     */
    static Jackson2JsonRedisSerializer getJackson2JsonRedisSerializer() {
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = null;
        jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        return jackson2JsonRedisSerializer;
    }

   /* 自定义LettuceConnectionFactory,这一步的作用就是返回根据你传入参数而配置的
    * LettuceConnectionFactory，
    * 也可以说是LettuceConnectionFactory的原理了，
    * 后面我会详细讲解的,各位同学也可先自己看看源码 这里定义的方法 createLettuceConnectionFactory，方便快速使用
   */
    private  LettuceConnectionFactory createLettuceConnectionFactory(
            int dbIndex, String hostName, int port, String password,
            int maxIdle,int minIdle,int maxActive,
            Long maxWait, Long timeOut,Long shutdownTimeOut){

        //redis配置
        RedisConfiguration redisConfiguration = new
                RedisStandaloneConfiguration(hostName,port);
        ((RedisStandaloneConfiguration) redisConfiguration).setDatabase(dbIndex);
        ((RedisStandaloneConfiguration) redisConfiguration).setPassword(password);

        //连接池配置
        GenericObjectPoolConfig genericObjectPoolConfig =
                new GenericObjectPoolConfig();
        genericObjectPoolConfig.setMaxIdle(maxIdle);
        genericObjectPoolConfig.setMinIdle(minIdle);
        genericObjectPoolConfig.setMaxTotal(maxActive);
        genericObjectPoolConfig.setMaxWaitMillis(maxWait);

        //redis客户端配置
        LettucePoolingClientConfiguration.LettucePoolingClientConfigurationBuilder
                builder =  LettucePoolingClientConfiguration.builder().
                commandTimeout(Duration.ofMillis(timeOut));

        builder.shutdownTimeout(Duration.ofMillis(shutdownTimeOut));
        builder.poolConfig(genericObjectPoolConfig);
        LettuceClientConfiguration lettuceClientConfiguration = builder.build();

        //根据配置和客户端配置创建连接
        LettuceConnectionFactory lettuceConnectionFactory = new
                LettuceConnectionFactory(redisConfiguration,lettuceClientConfiguration);
        lettuceConnectionFactory .afterPropertiesSet();

        return lettuceConnectionFactory;
    }

    /**
     *  配置 order RedisTemplate
     */
    @Bean("orderRedisTemplate")
    public RedisTemplate<String,Object> getOrderRedisTemplate(){
        return getRedisTemplate(orderDatabase);
    }

    /**
     *  配置 customer RedisTemplate
     */
    @Bean("customerRedisTemplate")
    public RedisTemplate<String,Object> getCustomerRedisTemplate(){
        return getRedisTemplate(customerDatabase);
    }

    @Bean("defRedisTemplate")
    public RedisTemplate<String, Object> getRedisDefTemplate() {
        return getRedisTemplate(database);
    }


    private RedisTemplate<String,Object> getRedisTemplate(int database){
        LettuceConnectionFactory lettuceConnectionFactory =
                createLettuceConnectionFactory
                        (database,hostName,port,password,maxIdle,minIdle,maxActive,maxWait,timeOut,shutdownTimeOut);
        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        /**
         * 使用 String 作为 Key 的序列化器,使用 Jackson 作为 Value 的序列化器
         */
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // key采用String的序列化方式
        redisTemplate.setKeySerializer(stringRedisSerializer);
        // hash的key也采用String的序列化方式
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        // value序列化方式采用jackson 这里先用stringRedisSerializer测试 实际业务中可以调用最上面的序列化方法
        redisTemplate.setValueSerializer(stringRedisSerializer);
        // hash的value序列化方式采用jackson 这里先用stringRedisSerializer测试 实际业务中可以调用最上面的序列化方法
        redisTemplate.setHashValueSerializer(stringRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }



}
