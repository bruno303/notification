package com.bso.notification.infra.gateway;

import com.bso.notification.application.notification.gateway.NotificationGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@ConditionalOnProperty(value = "app.notification.gateway", havingValue = "FAKE")
public class FakeNotificationGateway implements NotificationGateway {
    private static final Logger LOGGER = LoggerFactory.getLogger(FakeNotificationGateway.class);

    @Override
    public Mono<Void> send(String url, String body) {
        LOGGER.info("""
            Sending message
            Url: {}
            Body: {}
            """,
            url, body
        );
        return Mono.empty();
    }
}
