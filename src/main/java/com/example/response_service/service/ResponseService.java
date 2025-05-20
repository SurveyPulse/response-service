package com.example.response_service.service;

import com.example.global.exception.type.NotFoundException;
import com.example.response_service.client.service.ReportClientService;
import com.example.response_service.client.service.SurveyClientService;
import com.example.response_service.client.service.UserClientService;
import com.example.response_service.dto.client.AggregateRequest;
import com.example.response_service.dto.client.QuestionAnswerRequest;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ResponseService {

    private final ResponseRepository responseRepository;
    private final AnswerRepository answerRepository;
    private final UserClientService userClientService;
    private final SurveyClientService surveyClientService;
    private final ReportClientService reportClientService;

    @Transactional
    public void createResponse(CreateResponseRequest request) {
        log.info("설문 ID [{}]에 대한 응답 생성 시작, 응답자 ID: {}", request.surveyId(), request.respondentUserId());

        Response response = Response.builder()
                                    .surveyId(request.surveyId())
                                    .respondentUserId(request.respondentUserId())
                                    .submittedAt(LocalDateTime.now())
                                    .build();
        responseRepository.save(response);
        log.info("응답 생성 완료, 응답 ID: {}", response.getResponseId());

        List<Answer> answers = request.answers().stream()
                                      .map(dto -> {
                                          Answer a = Answer.builder()
                                                           .questionId(dto.questionId())
                                                           .answerContent(dto.answerContent())
                                                           .build();
                                          a.linkResponse(response);
                                          return a;
                                      })
                                      .toList();

        answerRepository.saveAll(answers);
        log.info("응답 ID [{}]에 대해 {}개의 답변 저장 완료", response.getResponseId(), answers.size());

        List<QuestionAnswerRequest> clientAnswers = answers.stream()
                                                           .map(answer -> new QuestionAnswerRequest(answer.getQuestionId(), answer.getAnswerContent()))
                                                           .toList();

        AggregateRequest aggregateRequest = new AggregateRequest(
                request.surveyId(),
                response.getResponseId(),
                request.respondentUserId(),
                clientAnswers
        );
        log.info("AggregateRequest 생성 완료: surveyId={}, responseId={}, userId={}, 답변 수={}",
                aggregateRequest.surveyId(), aggregateRequest.responseId(),
                aggregateRequest.userId(), aggregateRequest.answers().size());

        reportClientService.callAnalyzeAndAggregateReport(aggregateRequest);
    }

    @Transactional(readOnly = true)
    public List<ResponseDto> getResponsesBySurvey(Long surveyId, int pageNumber) {
        // 1) 응답 ID 페이징 조회
        Page<Long> responseIdPage = responseRepository.findIdsBySurveyId(surveyId, PageRequest.of(pageNumber, 20, Sort.by("responseId")));
        List<Long> responseIds = responseIdPage.getContent();
        if (responseIds.isEmpty()) {
            return List.of();
        }

        // 2) Response 엔티티 일괄 조회
        List<Response> responses = responseRepository.findAllById(responseIds);

        // 3) Answer 일괄 조회 및 그룹핑
        List<Answer> allAnswers = answerRepository.findAllByResponseIds(responseIds);
        Map<Long, List<Answer>> answersByResponseId = allAnswers.stream()
                                                                .collect(Collectors.groupingBy(answer -> answer.getResponse().getResponseId()));

        // 4) RespondentUserDto 일괄 조회 및 맵핑
        List<Long> respondentUserIds = responses.stream()
                                                .map(Response::getRespondentUserId)
                                                .distinct()
                                                .toList();
        List<RespondentUserDto> respondentUserDtos = userClientService.getRespondentUsersByIds(respondentUserIds);
        Map<Long, RespondentUserDto> userDtoById = respondentUserDtos.stream()
                                                                     .collect(Collectors.toMap(RespondentUserDto::userId, dto -> dto));

        // 5) QuestionWithSurveyDto 조회 (설문 전체 공통)
        List<QuestionWithSurveyDto> questionDtos = surveyClientService.getQuestionDtos(surveyId);

        // 6) 최종 ResponseDto 매핑
        return responses.stream()
                        .map(response -> ResponseDto.from(
                                response,
                                answersByResponseId.getOrDefault(response.getResponseId(), List.of()),
                                userDtoById.get(response.getRespondentUserId()),
                                questionDtos
                        ))
                        .toList();
    }

    @Transactional(readOnly = true)
    public ResponseDto getResponse(Long responseId) {
        log.info("응답 ID [{}] 단건 조회 시작", responseId);
        Response response = responseRepository.findById(responseId)
                                              .orElseThrow(() -> {
                                                  log.warn("응답 ID [{}]를 찾을 수 없습니다", responseId);
                                                  return new NotFoundException(ResponseExceptionType.RESPONSE_NOT_FOUND);
                                              });
        List<Answer> answers = answerRepository.findByResponseId(response.getResponseId());
        if (answers.isEmpty()) {
            log.warn("응답 ID [{}]에 대해 답변이 존재하지 않습니다", response.getResponseId());
            throw new NotFoundException(AnswerExceptionType.ANSWER_NOT_FOUND);
        }

        RespondentUserDto respondentUserDto = userClientService.getUserDto(response.getRespondentUserId());
        List<QuestionWithSurveyDto> questionDtos = surveyClientService.getQuestionDtos(response.getSurveyId());

        ResponseDto dto = ResponseDto.from(response, answers, respondentUserDto, questionDtos);
        log.info("응답 ID [{}]에 대한 ResponseDto 반환 완료", responseId);
        return dto;
    }

    @Transactional
    public ResponseDto updateResponse(Long responseId, UpdateResponseRequest request) {
        log.info("응답 ID [{}] 업데이트 시작", responseId);
        Response response = responseRepository.findById(responseId)
                                              .orElseThrow(() -> {
                                                  log.warn("응답 ID [{}] 업데이트 시 찾을 수 없습니다", responseId);
                                                  return new NotFoundException(ResponseExceptionType.RESPONSE_NOT_FOUND);
                                              });

        response.updateSubmittedAt(LocalDateTime.now());
        log.debug("응답 ID [{}]의 제출 시간 업데이트 완료", response.getResponseId());

        answerRepository.deleteByResponseId(responseId);
        log.debug("응답 ID [{}]에 대한 기존 답변 삭제 완료", responseId);

        List<Answer> answers = request.answers().stream()
                                      .map(dto -> {
                                          Answer a = Answer.builder()
                                                           .questionId(dto.questionId())
                                                           .answerContent(dto.answerContent())
                                                           .build();
                                          a.linkResponse(response);
                                          return a;
                                      })
                                      .toList();

        answerRepository.saveAll(answers);
        log.info("응답 ID [{}]에 대해 {}개의 새로운 답변 저장 완료", response.getResponseId(), answers.size());

        RespondentUserDto respondentUserDto = userClientService.getUserDto(response.getRespondentUserId());
        List<QuestionWithSurveyDto> questionDtos = surveyClientService.getQuestionDtos(response.getSurveyId());

        ResponseDto updatedDto = ResponseDto.from(response, answers, respondentUserDto, questionDtos);
        log.info("응답 ID [{}] 업데이트 성공", responseId);
        return updatedDto;
    }

    @Transactional
    public void deleteResponse(Long responseId) {
        log.info("응답 ID [{}] 삭제 시작", responseId);
        Response response = responseRepository.findById(responseId)
                                              .orElseThrow(() -> {
                                                  log.warn("응답 ID [{}] 삭제 시 찾을 수 없습니다", responseId);
                                                  return new NotFoundException(ResponseExceptionType.RESPONSE_NOT_FOUND);
                                              });
        answerRepository.deleteByResponseId(responseId);
        responseRepository.deleteById(responseId);
        log.info("응답 ID [{}] 및 관련 답변 삭제 완료", responseId);
    }
}
