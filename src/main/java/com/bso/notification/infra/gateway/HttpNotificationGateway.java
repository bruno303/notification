package com.bso.notification.infra.gateway;

import com.bso.notification.application.notification.gateway.NotificationGateway;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@ConditionalOnProperty(
    value = "app.notification.gateway",
    havingValue = "WEB_CLIENT",
    matchIfMissing = true
)
public class HttpNotificationGateway implements NotificationGateway {
    private final WebClient webClient = WebClient.create();

    @Override
    public Mono<Void> send(String url, String body) {
        return webClient.post()
            .uri(toUri(url))
            .bodyValue(body)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .retrieve()
            .bodyToMono(Object.class)
            .then();
    }

    @SneakyThrows
    private URI toUri(String url) {
        return new URI(url);
    }
}
