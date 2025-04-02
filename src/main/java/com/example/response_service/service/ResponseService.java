package com.example.response_service.service;

import com.example.global.exception.type.NotFoundException;
import com.example.response_service.client.service.SurveyClientService;
import com.example.response_service.client.service.UserClientService;
import com.example.response_service.dto.request.CreateResponseRequest;
import com.example.response_service.dto.request.UpdateResponseRequest;
import com.example.response_service.dto.response.QuestionWithSurveyDto;
import com.example.response_service.dto.response.RespondentUserDto;
import com.example.response_service.dto.response.ResponseDto;
import com.example.response_service.entity.Answer;
import com.example.response_service.entity.Response;
import com.example.response_service.exception.AnswerExceptionType;
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
    private final UserClientService userClientService;
    private final SurveyClientService surveyClientService;

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

        RespondentUserDto respondentUserDto = userClientService.getUserDto(request.respondentUserId());
        List<QuestionWithSurveyDto> questionDtos = surveyClientService.getQuestionDtos(request.surveyId());

        return ResponseDto.from(response, answers, respondentUserDto, questionDtos);
    }

    public List<ResponseDto> getResponsesBySurveyId(Long surveyId) {
        List<Response> responses = responseRepository.findBySurveyId(surveyId);
        if (responses.isEmpty()) {
            throw new NotFoundException(ResponseExceptionType.RESPONSE_NOT_FOUND);
        }
        return responses.stream().map(response -> {
            List<Answer> answers = answerRepository.findByResponseId(response.getResponseId());
            if (answers.isEmpty()) {
                throw new NotFoundException(AnswerExceptionType.ANSWER_NOT_FOUND);
            }

            RespondentUserDto respondentUserDto = userClientService.getUserDto(response.getRespondentUserId());
            List<QuestionWithSurveyDto> questionDtos = surveyClientService.getQuestionDtos(response.getSurveyId());

            return ResponseDto.from(response, answers, respondentUserDto, questionDtos);
        }).toList();
    }

    public ResponseDto getResponse(Long responseId) {
        Response response = responseRepository.findById(responseId)
                                              .orElseThrow(() -> new NotFoundException(ResponseExceptionType.RESPONSE_NOT_FOUND));
        List<Answer> answers = answerRepository.findByResponseId(response.getResponseId());
        if (answers.isEmpty()) {
            throw new NotFoundException(AnswerExceptionType.ANSWER_NOT_FOUND);
        }

        RespondentUserDto respondentUserDto = userClientService.getUserDto(response.getRespondentUserId());
        List<QuestionWithSurveyDto> questionDtos = surveyClientService.getQuestionDtos(response.getSurveyId());

        return ResponseDto.from(response, answers, respondentUserDto, questionDtos);
    }

    @Transactional
    public ResponseDto updateResponse(Long responseId, UpdateResponseRequest request) {
        Response response = responseRepository.findById(responseId)
                                              .orElseThrow(() -> new NotFoundException(ResponseExceptionType.RESPONSE_NOT_FOUND));

        response.updateSubmittedAt(LocalDateTime.now());

        answerRepository.deleteByResponseId(responseId);
        List<Answer> answers = request.answers().stream()
                                      .map(dto -> Answer.builder()
                                                        .responseId(response.getResponseId())
                                                        .questionId(dto.questionId())
                                                        .answerContent(dto.answerContent())
                                                        .build())
                                      .toList();
        answerRepository.saveAll(answers);

        RespondentUserDto respondentUserDto = userClientService.getUserDto(response.getRespondentUserId());
        List<QuestionWithSurveyDto> questionDtos = surveyClientService.getQuestionDtos(response.getSurveyId());

        return ResponseDto.from(response, answers, respondentUserDto, questionDtos);
    }

    @Transactional
    public void deleteResponse(Long responseId) {
        Response response = responseRepository.findById(responseId)
                                              .orElseThrow(() -> new NotFoundException(ResponseExceptionType.RESPONSE_NOT_FOUND));
        answerRepository.deleteByResponseId(responseId);
        responseRepository.deleteById(responseId);
    }
}
