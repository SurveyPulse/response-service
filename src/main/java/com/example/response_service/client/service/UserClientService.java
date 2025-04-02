package com.example.response_service.client.service;

import com.example.response_service.client.UserClient;
import com.example.response_service.dto.response.RespondentUserDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserClientService {

    private final UserClient userClient;

    @CircuitBreaker(name = "userService", fallbackMethod = "fallbackGetUserDto")
    public RespondentUserDto getUserDto(Long userId) {
        return userClient.getUserDto(userId);
    }

    public RespondentUserDto fallbackGetUserDto(Long userId, Throwable throwable) {
        log.error("UserClient 호출 실패 for userId {}: {}", userId, throwable.toString());
        return new RespondentUserDto(userId, "Unknown User");
    }
}
