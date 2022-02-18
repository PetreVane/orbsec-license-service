package com.orbsec.licensingservice.configs;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring")
@Getter @Setter
public class ServiceConfig {

    @Value("${spring.cloud.stream.bindings.input.destination}")
    private String organization_topic = "";

    @Value("${spring.redis.host}")
    private String redisServer = "";

    @Value("${spring.redis.port}")
    private String redisPort = "";
}
