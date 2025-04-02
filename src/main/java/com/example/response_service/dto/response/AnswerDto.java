package com.example.response_service.dto.response;

import com.example.response_service.entity.Answer;

public record AnswerDto(
        Long answerId,
        QuestionWithSurveyDto questionDto,
        String answerContent
) {
    public static AnswerDto from(Answer answer, QuestionWithSurveyDto questionDto) {
        return new AnswerDto(
                answer.getAnswerId(),
                questionDto,
                answer.getAnswerContent()
        );
    }
}
