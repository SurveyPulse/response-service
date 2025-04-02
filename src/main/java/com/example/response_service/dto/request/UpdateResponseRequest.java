package com.example.response_service.dto.request;

import java.time.LocalDateTime;
import java.util.List;

public record UpdateResponseRequest(
        LocalDateTime submittedAt,
        List<CreateAnswerDto> answers
) { }
