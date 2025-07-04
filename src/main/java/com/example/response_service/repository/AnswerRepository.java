package com.example.response_service.repository;

import com.example.response_service.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    @Query("SELECT a FROM Answer a JOIN FETCH a.response r WHERE r.responseId = :responseId")
    List<Answer> findByResponseId(@Param("responseId") Long responseId);

    @Query("SELECT a FROM Answer a JOIN FETCH a.response r WHERE r.responseId IN :responseIds")
    List<Answer> findByResponseIdIn(@Param("responseIds") List<Long> responseIds);

    @Query("SELECT a FROM Answer a WHERE a.response.responseId IN :responseIds")
    List<Answer> findAllByResponseIds(@Param("responseIds") List<Long> responseIds);

    @Modifying
    @Query("DELETE FROM Answer a WHERE a.response.responseId = :responseId")
    void deleteByResponseId(@Param("responseId") Long responseId);

}
