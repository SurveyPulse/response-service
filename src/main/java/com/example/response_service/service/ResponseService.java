package com.example.response_service.service;

import com.example.global.exception.type.NotFoundException;
import com.example.response_service.dto.request.CreateResponseRequest;
import com.example.response_service.dto.request.UpdateResponseRequest;
import com.example.response_service.dto.response.ResponseDto;
import com.example.response_service.entity.Answer;
import com.example.response_service.entity.Response;
import com.example.response_service.exception.ResponseExceptionType;
import com.example.response_service.repository.AnswerRepository;
import com.example.response_service.repository.ResponseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResponseService {

    private final ResponseRepository responseRepository;
    private final AnswerRepository answerRepository;

    @Transactional
    public ResponseDto createResponse(CreateResponseRequest request) {
        Response response = Response.builder()
                                    .surveyId(request.surveyId())
                                    .respondentUserId(request.respondentUserId())
                                    .submittedAt(LocalDateTime.now())
                                    .build();

        responseRepository.save(response);

        List<Answer> answers = request.answers().stream()
                                      .map(dto -> Answer.builder()
                                                        .responseId(response.getResponseId())
                                                        .questionId(dto.questionId())
                                                        .answerContent(dto.answerContent())
                                                        .build())
                                      .toList();
        answerRepository.saveAll(answers);

        return ResponseDto.from(response, answers);
    }

    public List<ResponseDto> getResponsesBySurveyId(Long surveyId) {
        List<Response> responses = responseRepository.findBySurveyId(surveyId);
        return responses.stream().map(response -> {
            List<Answer> answers = answerRepository.findByResponseId(response.getResponseId());
            return ResponseDto.from(response, answers);
        }).toList();
    }

    public ResponseDto getResponse(Long responseId) {
        Response response = responseRepository.findById(responseId)
                                              .orElseThrow(() -> new NotFoundException(ResponseExceptionType.RESPONSE_NOT_FOUND));
        List<Answer> answers = answerRepository.findByResponseId(response.getResponseId());
        return ResponseDto.from(response, answers);
    }

    @Transactional
    public ResponseDto updateResponse(Long responseId, UpdateResponseRequest request) {
        Response response = responseRepository.findById(responseId)
                                              .orElseThrow(() -> new NotFoundException(ResponseExceptionType.RESPONSE_NOT_FOUND));

        response.updateSubmittedAt(request.submittedAt());

        answerRepository.deleteByResponseId(responseId);
        List<Answer> answers = request.answers().stream()
                                      .map(dto -> Answer.builder()
                                                        .responseId(response.getResponseId())
                                                        .questionId(dto.questionId())
                                                        .answerContent(dto.answerContent())
                                                        .build())
                                      .toList();
        answerRepository.saveAll(answers);

        return ResponseDto.from(response, answers);
    }

    @Transactional
    public void deleteResponse(Long responseId) {
        answerRepository.deleteByResponseId(responseId);
        responseRepository.deleteById(responseId);
    }

}
