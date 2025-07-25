package com.jobportal.company;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CompanyServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CompanyServiceApplication.class, args);
    }
}