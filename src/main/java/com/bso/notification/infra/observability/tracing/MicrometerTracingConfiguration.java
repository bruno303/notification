package com.bso.notification.infra.observability.tracing;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.observation.DefaultMeterObservationHandler;
import io.micrometer.observation.ObservationHandler;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.handler.DefaultTracingObservationHandler;
import io.micrometer.tracing.handler.PropagatingReceiverTracingObservationHandler;
import io.micrometer.tracing.handler.PropagatingSenderTracingObservationHandler;
import io.micrometer.tracing.propagation.Propagator;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MicrometerTracingConfiguration {
    private final MeterRegistry meterRegistry;
    private final ObservationRegistry registry;
    private final Propagator propagator;
    private final Tracer tracer;
    private final ApplicationProperties applicationProperties;

    @PostConstruct
    public void configureObservationRegistry() {
        registry.observationConfig()
            .observationHandler(new DefaultMeterObservationHandler(meterRegistry))
            .observationFilter(new CustomTagsObservationFilter(applicationProperties))
            .observationHandler(new ObservationHandler.FirstMatchingCompositeObservationHandler(
                new PropagatingSenderTracingObservationHandler<>(tracer, propagator),
                new PropagatingReceiverTracingObservationHandler<>(tracer, propagator),
                new DefaultTracingObservationHandler(tracer)));
    }
}
