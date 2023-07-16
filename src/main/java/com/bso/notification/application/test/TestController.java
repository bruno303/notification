package com.bso.notification.application.test;

import com.bso.notification.domain.notification.enums.Entity;
import com.bso.notification.domain.notification.enums.Event;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/test")
@Profile("local")
public class TestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);

    @PostConstruct
    public void init() {
        LOGGER.info("TestController loaded");
    }

    @PostMapping("notification/{entity}/{event}")
    public Mono<Void> receiveTest(
        @PathVariable Entity entity,
        @PathVariable Event event,
        @RequestBody TestBodyDto body,
        @RequestHeader Map<String, String> headers
    ) {
        LOGGER.info(
            "Received test message with entity '{}', event '{}' ,body '{}' and headers '{}'",
            entity,
            event,
            body,
            headers
        );
        return Mono.empty();
    }

    public record TestBodyDto(String message) {}
}
