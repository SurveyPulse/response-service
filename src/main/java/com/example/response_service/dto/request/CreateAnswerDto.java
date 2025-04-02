package com.example.response_service.dto.request;

public record CreateAnswerDto(
        Long questionId,
        String answerContent
) { }
