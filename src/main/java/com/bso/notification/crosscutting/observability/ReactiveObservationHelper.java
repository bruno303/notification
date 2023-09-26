package com.bso.notification.crosscutting.observability;

import io.micrometer.context.ContextSnapshot;
import io.micrometer.context.ContextSnapshotFactory;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.contextpropagation.ObservationThreadLocalAccessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.context.ContextView;

import java.util.Objects;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class ReactiveObservationHelper {
    private final ObservationRegistry observationRegistry;

    /**
     * Starts a new observation and handle the thread local to context mapping.
     * Every step when logging occurs, its important to run like:
     *
     * <pre>obs.scoped(() -> LOGGER.info("Hello"))</pre>
     */
    public <T> Mono<T> runObserved(String name, Function<Observation, Mono<T>> action) {
        return Mono.deferContextual(ctx -> {
            try (var ignored = createScope(ctx)) {
                return executeObservedMono(getObservation(name), action);
            }
        });
    }

    /**
     * Starts a new observation and handle the thread local {@code <->} context mapping.
     * Every step when logging occurs, its important to run like:
     *
     * <pre>obs.scoped(() -> LOGGER.info("Hello"))</pre>
     */
    public <T> Flux<T> runObservedMany(String name, Function<Observation, Flux<T>> action) {
        return Flux.deferContextual(ctx -> {
            try (var ignored = createScope(ctx)) {
                return executeObservedFlux(getObservation(name), action);
            }
        });
    }

    private Observation getObservation(String name) {
        var currentObservation = observationRegistry.getCurrentObservation();
        return Objects.requireNonNullElseGet(
            currentObservation,
            () -> Observation.start(name, observationRegistry)
        );
    }

    private ContextSnapshot.Scope createScope(ContextView ctx) {
        return ContextSnapshotFactory.builder().build().setThreadLocalsFrom(ctx, ObservationThreadLocalAccessor.KEY);
    }

    private <T> Mono<T> executeObservedMono(Observation observation, Function<Observation, Mono<T>> action) {
        return Mono.just(observation).flatMap(obs -> obs.scoped(() -> action.apply(obs)))
            .doOnError(ex -> {
                observation.error(ex);
                observation.stop();
            })
            .doOnSuccess(result -> observation.stop());
    }

    private <T> Flux<T> executeObservedFlux(Observation observation, Function<Observation, Flux<T>> action) {
        return Mono.just(observation).flatMapMany(obs -> obs.scoped(() -> action.apply(obs)))
            .doOnError(observation::error)
            .doFinally(signalType -> observation.stop());
    }
}
