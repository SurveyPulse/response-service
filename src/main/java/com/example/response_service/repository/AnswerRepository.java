package com.example.response_service.repository;

import com.example.response_service.entity.Answer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    @EntityGraph(attributePaths = "response")
    List<Answer> findByResponseId(Long responseId);

    void deleteByResponseId(Long responseId);

}
