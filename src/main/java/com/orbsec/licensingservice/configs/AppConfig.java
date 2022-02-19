package com.orbsec.licensingservice.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;


@Configuration
public class AppConfig {

    private final ServiceConfig serviceConfig;

    @Autowired
    public AppConfig(ServiceConfig serviceConfig) {
        this.serviceConfig = serviceConfig;
    }


    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        String hostName = serviceConfig.getRedisServer();
        var port = Integer.parseInt(serviceConfig.getRedisPort());
        return new LettuceConnectionFactory(hostName, port);
    }


    @Bean
    RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }

}
