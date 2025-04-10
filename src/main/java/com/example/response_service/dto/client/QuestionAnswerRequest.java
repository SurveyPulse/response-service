package com.example.response_service.dto.client;

public record QuestionAnswerRequest(
        Long questionId,
        String text
) {}
