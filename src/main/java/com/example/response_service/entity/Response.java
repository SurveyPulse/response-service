package com.example.response_service.entity;

import com.example.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "responses")
public class Response extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long responseId;

    @Column(nullable = false)
    private Long surveyId;

    @Column(nullable = false)
    private Long respondentUserId;

    @Column(nullable = false)
    private LocalDateTime submittedAt;

    @Builder
    public Response(Long surveyId, Long respondentUserId, LocalDateTime submittedAt) {
        this.surveyId = surveyId;
        this.respondentUserId = respondentUserId;
        this.submittedAt = submittedAt;
    }

    public void updateSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }
}
