package com.bso.notification.application.notification.metric;

import com.bso.notification.crosscutting.observability.MetricRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class NotificationMetricRegistry {
    private final MetricRegistry metricRegistry;
    private static final String NOTIFICATION_COUNTER = "notification-counter";
    private static final String NOTIFICATION_ERROR_COUNTER = "notification-error-counter";

    public Mono<Void> incrementNotificationCounter() {
        return metricRegistry.incrementMetric(NOTIFICATION_COUNTER);
    }

    public Mono<Void> incrementErrorCounter() {
        return metricRegistry.incrementMetric(NOTIFICATION_ERROR_COUNTER);
    }
}
