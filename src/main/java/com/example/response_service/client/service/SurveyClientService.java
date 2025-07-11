package com.example.response_service.client.service;

import com.example.response_service.client.SurveyClient;
import com.example.response_service.dto.response.QuestionWithSurveyDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SurveyClientService {

    private final SurveyClient surveyClient;

    @CircuitBreaker(name = "surveyService", fallbackMethod = "fallbackGetQuestionDtos")
    @Retry(name = "surveyService", fallbackMethod = "fallbackGetQuestionDtos")
    @TimeLimiter(name = "surveyService")
    public List<QuestionWithSurveyDto> getQuestionDtos(Long surveyId) {
        return surveyClient.getQuestionDtos(surveyId);
    }

    public List<QuestionWithSurveyDto> fallbackGetQuestionDtos(Long surveyId, Throwable throwable) {
        return Collections.emptyList();
    }
}
