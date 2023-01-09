package com.yanggu.metric_calculate.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * redis配置序列化和反序列化方式
 * <p><a href="https://www.jianshu.com/p/50b80b00039c">Redis配置Kryo序列化和Snappy压缩</a>
 */
@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> kryoRedisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        //key采用String的序列化方式
        template.setKeySerializer(new StringRedisSerializer());
        //value采用字节数组的方式
        template.setValueSerializer(RedisSerializer.byteArray());
        template.afterPropertiesSet();
        return template;
    }

}