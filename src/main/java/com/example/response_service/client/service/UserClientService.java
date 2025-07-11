package com.example.response_service.client.service;

import com.example.response_service.client.UserClient;
import com.example.response_service.dto.response.RespondentUserDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserClientService {

    private final UserClient userClient;

    @CircuitBreaker(name = "userService", fallbackMethod = "fallbackGetUserDto")
    @Retry(name = "userService", fallbackMethod = "fallbackGetUserDto")
    @TimeLimiter(name = "userService")
    public RespondentUserDto getUserDto(Long userId) {
        return userClient.getUserDto(userId);
    }

    @CircuitBreaker(name = "userService", fallbackMethod = "fallbackGetRespondentUsersByIds")
    @Retry(name = "userService", fallbackMethod = "fallbackGetRespondentUsersByIds")
    @TimeLimiter(name = "userService")
    public List<RespondentUserDto> getRespondentUsersByIds(List<Long> userIds) {
        return userClient.getRespondentUsersByIds(userIds);
    }

    public RespondentUserDto fallbackGetUserDto(Long userId, Throwable throwable) {
        log.warn("[fallbackGetUserDto] userId={} fallback: {}", userId, throwable.toString());
        return new RespondentUserDto(userId, "Unknown User", "Unknown Role");
    }

    public List<RespondentUserDto> fallbackGetRespondentUsersByIds(List<Long> userIds, Throwable throwable) {
        log.warn("[fallbackGetRespondentUsersByIds] userIds={} fallback: {}", userIds, throwable.toString());
        return userIds.stream()
                      .map(id -> new RespondentUserDto(id, "Unknown User", "Unknown Role"))
                      .toList();
    }
}
