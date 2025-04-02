package com.example.response_service.controller;

import com.example.response_service.dto.request.CreateResponseRequest;
import com.example.response_service.dto.response.ResponseDto;
import com.example.response_service.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/responses")
public class ResponseController {

    private final ResponseService responseService;

    @PostMapping
    public ResponseEntity<ResponseDto> createResponse(@RequestBody CreateResponseRequest request) {
        ResponseDto responseDto = responseService.createResponse(request);
        return ResponseEntity.ok(responseDto);
    }

}
