package com.bso.notification.web.controller.test;

import com.bso.notification.crosscutting.observability.ReactiveObservationHelper;
import com.bso.notification.domain.notification.enums.Entity;
import com.bso.notification.domain.notification.enums.Event;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/test")
@Profile("local")
@RequiredArgsConstructor
public class TestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);
    private final ReactiveObservationHelper observationHelper;

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
        return observationHelper.runObserved(
            "TestController.receiveTest",
        obs -> {
            obs.scoped(() ->
                LOGGER.info(
                    "Received test message with entity '{}', event '{}' ,body '{}' and headers '{}'",
                    entity,
                    event,
                    body,
                    headers
                )
            );
            return Mono.empty();
        });
    }

    public record TestBodyDto(String message) {}
}
