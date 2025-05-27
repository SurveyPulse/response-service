# MSA 기반 SurveyPulse 응답 서비스

SurveyPulse 플랫폼의 설문 응답(Response) 관리를 담당하는 마이크로서비스입니다. 응답 생성, 조회, 수정, 삭제 및 집계 연동 기능을 제공합니다.

## 주요 기능

- **응답 생성** (`POST /api/responses`)
  - 요청 본문으로 설문 ID, 응답자 ID, 문항별 답변 목록 전송
  - `Response` 엔티티 및 `Answer` 엔티티 저장
  - 외부 Report 서비스로 `AggregateRequest` 전송 (FeignClient + Resilience4j)

- **설문별 응답 목록 조회** (`GET /api/responses/survey/{surveyId}?page={page}`)
  - 페이징 처리된 응답 ID 조회
  - 응답 정보, 답변 목록, 응답자 사용자명, 설문 문항 정보 결합 후 반환
  - 사용자 정보 조회(OpenFeign + Resilience4j)
  - 설문 문항 조회(OpenFeign + Resilience4j)
  - Redis(AWS ElastiCache) 캐싱 적용 가능

- **단일 응답 조회** (`GET /api/responses/{responseId}`)
  - 응답 및 답변 조회
  - 응답자 정보 및 설문 문항 정보 포함 후 반환

- **응답 수정** (`PUT /api/responses/{responseId}`)
  - 제출 시간 갱신, 기존 답변 삭제 및 새 답변 저장
  - 응답 DTO 반환

- **응답 삭제** (`DELETE /api/responses/{responseId}`)
  - 답변 및 응답 엔티티 삭제

## 기술 스펙

- **언어 & 프레임워크**: Java, Spring Boot
- **데이터베이스**: Spring Data JPA, MySQL (AWS RDS)
- **HTTP 클라이언트**: OpenFeign
- **회로 차단기 & 복원력**: Resilience4j
- **외부 연동**:
  - User, Survey, Report 서비스 간 Feign 통신
  - JWT 기반 인증 토큰 전파
- **보안**: Spring Security, JWT
- **로깅 & 모니터링**: Elasticsearch, Logstash, Kibana (ELK), Prometheus, Grafana
- **CI/CD**: GitHub Actions
- **컨테이너 & 오케스트레이션**: Docker, Kubernetes, Helm, AWS EKS
- **아키텍처**: 마이크로서비스 아키텍처(MSA)

## 아키텍처

![서비스 아키텍처 다이어그램](https://github.com/SurveyPulse/user-service/blob/main/docs/images/aws-architecture.png)
![RDS 아키텍처 다이어그램](https://github.com/SurveyPulse/user-service/blob/main/docs/images/aws-rds-architecture.png)


## CI/CD 아키텍처
![CI/CD 파이프라인 다이어그램](https://github.com/SurveyPulse/user-service/blob/main/docs/images/cicd-architecture.png)