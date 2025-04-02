package com.example.response_service.dto.response;

public record RespondentUserDto(
        Long userId,
        String username,
        String userRole
) {
}
