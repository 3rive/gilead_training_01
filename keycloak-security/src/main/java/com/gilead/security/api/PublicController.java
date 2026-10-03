package com.gilead.security.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class PublicController {

    @GetMapping("/api/public/health")
    Map<String, String> health() {
        return Map.of("status", "up");
    }
}
