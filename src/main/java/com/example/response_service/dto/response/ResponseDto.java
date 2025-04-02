package com.example.response_service.dto.response;

import com.example.response_service.entity.Response;
import com.example.response_service.entity.Answer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public record ResponseDto(
        Long responseId,
        Long surveyId,
        RespondentUserDto respondentUserDto,
        LocalDateTime submittedAt,
        List<AnswerDto> answers
) {
    public static ResponseDto from(
            Response response,
            List<Answer> answers,
            RespondentUserDto respondentUserDto,
            List<QuestionWithSurveyDto> questionDtos
    ) {
        // List<QuestionWithSurveyDto>를 Map으로 변환 (questionId -> QuestionWithSurveyDto)
        Map<Long, QuestionWithSurveyDto> questionMap = questionDtos.stream()
                                                                   .collect(Collectors.toMap(QuestionWithSurveyDto::questionId, Function.identity()));

        List<AnswerDto> answerDtos = answers.stream()
                                            .map(answer -> {
                                                QuestionWithSurveyDto questionDto = questionMap.get(answer.getQuestionId());
                                                if (questionDto == null) {
                                                    throw new IllegalArgumentException("No question info for question id: " + answer.getQuestionId());
                                                }
                                                return AnswerDto.from(answer, questionDto);
                                            })
                                            .collect(Collectors.toList());

        return new ResponseDto(
                response.getResponseId(),
                response.getSurveyId(),
                respondentUserDto,
                response.getSubmittedAt(),
                answerDtos
        );
    }
}
