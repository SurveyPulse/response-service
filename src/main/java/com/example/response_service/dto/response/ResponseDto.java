package com.example.response_service.dto.response;

import com.example.response_service.entity.Response;
import com.example.response_service.entity.Answer;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record ResponseDto(
        Long responseId,
        Long surveyId,
        Long respondentUserId,
        LocalDateTime submittedAt,
        List<AnswerDto> answers
) {
    public static ResponseDto from(Response response, List<Answer> answers) {
        List<AnswerDto> answerDtos = answers.stream()
                                            .map(AnswerDto::from)
                                            .collect(Collectors.toList());
        return new ResponseDto(
                response.getResponseId(),
                response.getSurveyId(),
                response.getRespondentUserId(),
                response.getSubmittedAt(),
                answerDtos
        );
    }
}
