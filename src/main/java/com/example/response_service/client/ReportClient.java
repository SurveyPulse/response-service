package com.example.response_service.client;

import com.example.response_service.client.config.FeignClientConfig;
import com.example.response_service.dto.client.AggregateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "report-service", url = "${feign.report-service-url}", configuration = FeignClientConfig.class)
public interface ReportClient {

    @PostMapping("/api/reports/analyze")
    void callAnalyzeAndAggregateReport(@RequestBody AggregateRequest aggregateRequest);

}
