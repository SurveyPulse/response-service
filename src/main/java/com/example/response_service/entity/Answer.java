package com.example.response_service.entity;

import com.example.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "answers")
public class Answer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long answerId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "response_id", nullable = false)
    private Response response;

    @Column(nullable = false)
    private Long questionId;

    @Column(columnDefinition = "TEXT")
    private String answerContent;

    @Builder
    public Answer(Long questionId, String answerContent) {
        this.questionId = questionId;
        this.answerContent = answerContent;
    }

    public void linkResponse(Response response) {
        this.response = response;
        response.getAnswers().add(this);
    }
}
