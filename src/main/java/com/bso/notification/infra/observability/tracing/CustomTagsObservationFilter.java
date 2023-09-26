package com.bso.notification.infra.observability.tracing;

import io.micrometer.common.KeyValue;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationFilter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomTagsObservationFilter implements ObservationFilter {
    private final ApplicationProperties applicationProperties;

    @Override
    public Observation.Context map(Observation.Context context) {
        return context
            .addHighCardinalityKeyValue(KeyValue.of("app", applicationProperties.name()))
            .addHighCardinalityKeyValue(KeyValue.of("env", applicationProperties.env()));
    }
}
