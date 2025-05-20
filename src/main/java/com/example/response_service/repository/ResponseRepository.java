package com.example.response_service.repository;

import com.example.response_service.entity.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ResponseRepository extends JpaRepository<Response, Long> {

    @Query("SELECT r.responseId FROM Response r WHERE r.surveyId = :surveyId ORDER BY r.responseId ASC")
    Page<Long> findIdsBySurveyId(@Param("surveyId") Long surveyId, Pageable pageable);

}
