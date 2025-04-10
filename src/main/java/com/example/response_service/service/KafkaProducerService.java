package com.example.response_service.service;

import com.example.response_service.dto.client.AggregateRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void sendAggregateRequest(String topic, AggregateRequest aggregateRequest) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(aggregateRequest);
            kafkaTemplate.send(topic, jsonMessage);
        } catch (JsonProcessingException e) {
            log.error("AggregateRequest 직렬화 실패", e);
        }
    }
}
