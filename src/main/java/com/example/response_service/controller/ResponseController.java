package com.example.response_service.controller;

import com.example.response_service.dto.client.AggregateRequest;
import com.example.response_service.dto.request.CreateResponseRequest;
import com.example.response_service.dto.request.UpdateResponseRequest;
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
    public ResponseEntity<AggregateRequest> createResponse(@RequestBody CreateResponseRequest request) {
        AggregateRequest aggregateRequest = responseService.createResponse(request);
        return ResponseEntity.ok(aggregateRequest);
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

    @PutMapping("/{responseId}")
    public ResponseEntity<ResponseDto> updateResponse(@PathVariable Long responseId,
                                                      @RequestBody UpdateResponseRequest request) {
        ResponseDto updatedResponse = responseService.updateResponse(responseId, request);
        return ResponseEntity.ok(updatedResponse);
    }

    @DeleteMapping("/{responseId}")
    public ResponseEntity<Void> deleteResponse(@PathVariable Long responseId) {
        responseService.deleteResponse(responseId);
        return ResponseEntity.noContent().build();
    }

}
