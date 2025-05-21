package com.example.response_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateAnswerDto(
        @NotNull(message = "questionId는 필수입니다.")
        Long questionId,

        @NotBlank(message = "답변 내용은 빈 값일 수 없습니다.")
        @Size(max = 500, message = "답변 내용은 최대 500자까지 입력 가능합니다.")
        String answerContent
) { }
