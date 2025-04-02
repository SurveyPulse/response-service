package com.example.response_service.dto.request;

import java.util.List;

public record CreateResponseRequest(
        Long surveyId,
        Long respondentUserId,
        List<CreateAnswerDto> answers
) { }
