package com.example.response_service.client.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    // aggregate-request-topic 토픽을 파티션 3, 복제 3으로 생성
    @Bean
    public NewTopic aggregateRequestTopic() {
        return TopicBuilder.name("aggregate-request-topic")
                           .partitions(3)
                           .replicas(3)
                           .build();
    }
}

