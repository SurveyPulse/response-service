package com.example.response_service.client;

import com.example.response_service.client.config.FeignClientConfig;
import com.example.response_service.dto.response.QuestionWithSurveyDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "survey-service", url = "${feign.survey-service-url}", configuration = FeignClientConfig.class)
public interface SurveyClient {

    @GetMapping("/api/surveys/{surveyId}/questions")
    List<QuestionWithSurveyDto> getQuestionDtos(@PathVariable("surveyId") Long surveyId);

}
