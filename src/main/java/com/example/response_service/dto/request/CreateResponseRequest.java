package com.example.response_service.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record CreateResponseRequest(
        @NotNull(message = "surveyId는 필수입니다.")
        Long surveyId,

        @NotNull(message = "respondentUserId는 필수입니다.")
        Long respondentUserId,

        @NotEmpty(message = "answers는 최소 하나 이상 필요합니다.")
        @Valid
        List<CreateAnswerDto> answers
) { }
