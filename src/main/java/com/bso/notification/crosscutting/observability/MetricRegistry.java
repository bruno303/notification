package com.bso.notification.crosscutting.observability;

import reactor.core.publisher.Mono;

public interface MetricRegistry {
    Mono<Void> incrementMetric(String name);
    Mono<Void> incrementMetric(String name, long amount);
    Mono<Void> gauge(String name, long amount);
}
