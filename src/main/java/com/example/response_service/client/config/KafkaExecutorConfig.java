package com.example.response_service.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class KafkaExecutorConfig {

    @Bean
    public ThreadPoolTaskExecutor kafkaExecutor() {
        ThreadPoolTaskExecutor exec = new ThreadPoolTaskExecutor();
        exec.setCorePoolSize(20);
        exec.setMaxPoolSize(50);
        exec.setQueueCapacity(500);
        exec.setThreadNamePrefix("kafka-producer-");
        exec.initialize();
        return exec;
    }

}
