package com.example.response_service.client.service;

import com.example.response_service.client.ReportClient;
import com.example.response_service.dto.client.AggregateRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportClientService {

    private final ReportClient reportClient;

    @CircuitBreaker(name = "report-service", fallbackMethod = "fallbackCallAnalyzeAndAggregateReport")
    @Retry(name = "reportService", fallbackMethod = "fallbackCallAnalyzeAndAggregateReport")
    @TimeLimiter(name = "reportService")
    public void callAnalyzeAndAggregateReport(AggregateRequest aggregateRequest) {
        reportClient.callAnalyzeAndAggregateReport(aggregateRequest);
    }

    public void fallbackCallAnalyzeAndAggregateReport(AggregateRequest aggregateRequest, Throwable throwable) {
        log.error("서킷 브레이커가 동작했습니다. AggregateRequest: {}에 대해 대체 동작을 수행합니다. 사유: {}", aggregateRequest, throwable.getMessage());
    }
}
