package com.bso.notification.application.notification.gateway;

import reactor.core.publisher.Mono;

public interface NotificationGateway {
    Mono<Void> send(String url, String body);
}
