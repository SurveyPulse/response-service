package com.example.response_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@EnableDiscoveryClient
@EnableFeignClients
@ComponentScan(basePackages = {"com.example.response_service", "com.example.global"})
public class ResponseServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResponseServiceApplication.class, args);
	}

}
