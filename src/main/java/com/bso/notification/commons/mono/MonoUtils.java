package com.bso.notification.commons.mono;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

import static com.bso.notification.commons.exception.ExceptionUtils.error;

public final class MonoUtils {
    private MonoUtils() {}

    public static <T> Mono<T> applyDelay(Duration duration) {
        return Mono
                .delay(duration, Schedulers.boundedElastic())
                .then(Mono.empty());
    }

    public static <T> Mono<T> monoError(String messageFormat, Object... args) {
        return Mono.error(error(messageFormat, args));
    }
}
