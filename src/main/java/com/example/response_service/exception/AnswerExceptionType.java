package com.example.response_service.exception;

import com.example.global.exception.ExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AnswerExceptionType implements ExceptionType {
    ANSWER_NOT_FOUND(5101, "해당 답변을 찾을 수 없습니다."),
    INVALID_ANSWER_FORMAT(5102, "답변 형식이 올바르지 않습니다.");

    private final int statusCode;
    private final String message;
}
