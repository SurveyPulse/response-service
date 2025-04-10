package com.example.response_service.dto.client;

import java.util.List;

public record AggregateRequest(
        Long surveyId,
        Long responseId,
        Long userId,
        List<QuestionAnswerRequest> answers
) {}
