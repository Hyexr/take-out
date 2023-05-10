package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 黄烨轩
 * @version : 1.0
 * @Project : Spring_demo
 * @Package : com.example.config
 * @ClassName : RedisConfig.java
 * @createTime : 2023/5/10 10:16
 */
@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        // 创建RedisTemplate对象
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // 设置连接工厂
        template.setConnectionFactory(redisConnectionFactory);
        // 设置key的序列化器
        template.setKeySerializer(new StringRedisSerializer());
//        // 设置value的序列化器
//        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

}
