package com.bso.notification.async.impl;

import com.bso.notification.async.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component("SendNotificationHandler")
public class SendNotificationMessageHandler implements MessageHandler {
    private final Logger LOGGER = LoggerFactory.getLogger(SendNotificationMessageHandler.class);

    @Override
    public Mono<Void> handle(String message) {
        LOGGER.info("Received message '{}'", message);
//        return Mono.error(new RuntimeException("Teste"));
        return Mono.empty();
    }
}
