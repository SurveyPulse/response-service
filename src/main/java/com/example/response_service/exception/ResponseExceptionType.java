package com.example.response_service.exception;

import com.example.global.exception.ExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseExceptionType implements ExceptionType {
    RESPONSE_NOT_FOUND(5201, "해당 응답을 찾을 수 없습니다."),
    RESPONSE_CREATION_FAILED(5202, "응답 생성에 실패하였습니다.");

    private final int statusCode;
    private final String message;
}
