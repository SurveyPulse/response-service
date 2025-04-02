package com.example.response_service.dto.request;

import java.util.List;

public record UpdateResponseRequest(
        List<CreateAnswerDto> answers
) { }
