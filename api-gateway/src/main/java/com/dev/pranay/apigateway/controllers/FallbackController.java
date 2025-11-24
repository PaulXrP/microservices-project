package com.dev.pranay.apigateway.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FallbackController {

    @RequestMapping("/fallback/product")
    public Mono<String> productServiceFallback() {
        return Mono.just("⚠️ Oops! The Product Service is currently taking too long to respond or is down. Please try again later.");
    }
}
