package com.bso.notification.commons.mono;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

public final class MonoUtils {
    private MonoUtils() {}

    public static <T> Mono<T> applyDelay(Duration duration, Class<T> clazz) {
        return Mono
                .delay(duration, Schedulers.boundedElastic())
                .then()
                .cast(clazz);
    }
}
