package com.example.response_service.repository;

import com.example.response_service.entity.Response;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResponseRepository extends JpaRepository<Response, Long> {

    List<Response> findBySurveyId(Long surveyId);
    
}
