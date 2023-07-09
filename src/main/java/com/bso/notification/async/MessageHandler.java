package com.bso.notification.async;

import reactor.core.publisher.Mono;

public interface MessageHandler {
    Mono<Void> handle(String message);
}
