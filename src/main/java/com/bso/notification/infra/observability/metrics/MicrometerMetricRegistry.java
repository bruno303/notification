package com.bso.notification.infra.observability.metrics;

import com.bso.notification.crosscutting.observability.MetricRegistry;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class MicrometerMetricRegistry implements MetricRegistry {
    private final MeterRegistry meterRegistry;

    @Override
    public Mono<Void> incrementMetric(String name) {
        return incrementMetric(name, 1);
    }

    @Override
    public Mono<Void> incrementMetric(String name, long amount) {
        meterRegistry.counter(name).increment(amount);
        return Mono.empty();
    }

    @Override
    public Mono<Void> gauge(String name, long amount) {
        meterRegistry.gauge(name, amount);
        return Mono.empty();
    }
}
