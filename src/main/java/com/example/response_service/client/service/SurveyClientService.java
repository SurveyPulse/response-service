package com.example.response_service.client.service;

import com.example.response_service.client.SurveyClient;
import com.example.response_service.dto.response.QuestionWithSurveyDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SurveyClientService {

    private final SurveyClient surveyClient;

    @CircuitBreaker(name = "surveyService", fallbackMethod = "fallbackGetQuestionDtos")
    public List<QuestionWithSurveyDto> getQuestionDtos(Long surveyId) {
        return surveyClient.getQuestionDtos(surveyId);
    }

    public List<QuestionWithSurveyDto> fallbackGetQuestionDtos(Long surveyId, Throwable throwable) {
        return Collections.emptyList();
    }
}
