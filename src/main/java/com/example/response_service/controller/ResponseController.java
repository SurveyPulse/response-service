package com.example.response_service.controller;

import com.example.response_service.dto.request.CreateResponseRequest;
import com.example.response_service.dto.response.ResponseDto;
import com.example.response_service.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/survey/{surveyId}")
    public ResponseEntity<List<ResponseDto>> getResponsesBySurveyId(@PathVariable Long surveyId) {
        List<ResponseDto> responses = responseService.getResponsesBySurveyId(surveyId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{responseId}")
    public ResponseEntity<ResponseDto> getResponse(@PathVariable Long responseId) {
        ResponseDto responseDto = responseService.getResponse(responseId);
        return ResponseEntity.ok(responseDto);
    }

}
