package com.fxneira.demo.stream.controllers;

import com.fxneira.demo.stream.dtos.HealthDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/ping")
    public HealthDto  ping() {
        return new HealthDto("Pong");
    }

    @GetMapping("/health")
    public HealthDto  health() {
        return new HealthDto("UP");
    }

    @GetMapping("/")
    public HealthDto welcome() {
        return new HealthDto("Welcome to the demo application!");
    }
}
