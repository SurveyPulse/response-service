package com.example.response_service.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record UpdateResponseRequest(
        @NotEmpty(message = "answers는 최소 하나 이상 필요합니다.")
        @Valid
        List<CreateAnswerDto> answers
) { }
