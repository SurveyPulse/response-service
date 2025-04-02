package com.example.response_service.client;

import com.example.response_service.client.config.FeignClientConfig;
import com.example.response_service.dto.response.RespondentUserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "${feign.user-service-url}", configuration = FeignClientConfig.class)
public interface UserClient {

    @GetMapping("/api/users/{userId}")
    RespondentUserDto getUserDto(@PathVariable("userId") Long userId);

}
