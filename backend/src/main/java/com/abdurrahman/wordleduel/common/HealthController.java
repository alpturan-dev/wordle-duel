package com.abdurrahman.wordleduel.common;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HealthController {

    public record HealthResponse(String status, long serverTime) {
    }

    @GetMapping("/health")
    public HealthResponse health() {
        return new HealthResponse("UP", System.currentTimeMillis());
    }
}
