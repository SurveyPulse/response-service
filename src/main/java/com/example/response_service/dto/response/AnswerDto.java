package com.example.response_service.dto.response;

import com.example.response_service.entity.Answer;

public record AnswerDto(
        Long answerId,
        Long questionId,
        String answerContent
) {
    public static AnswerDto from(Answer answer) {
        return new AnswerDto(
                answer.getAnswerId(),
                answer.getQuestionId(),
                answer.getAnswerContent()
        );
    }
}
