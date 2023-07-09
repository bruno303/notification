package com.bso.notification.web.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(IllegalStateException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleIllegalState(
        IllegalStateException ex,
        ServerHttpRequest request
    ) {
        var body = new ErrorResponse(
            ex.getMessage(),
            LocalDateTime.now(),
            request.getURI().getPath()
        );
        return Mono.just(ResponseEntity.badRequest().body(body));
    }
}
