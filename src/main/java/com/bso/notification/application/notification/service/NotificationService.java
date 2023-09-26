package com.bso.notification.application.notification.service;

import com.bso.notification.application.client.repository.ClientRepository;
import com.bso.notification.application.notification.command.SendNotificationCommand;
import com.bso.notification.application.notification.gateway.NotificationGateway;
import com.bso.notification.application.notification.metric.NotificationMetricRegistry;
import com.bso.notification.crosscutting.observability.ReactiveObservationHelper;
import com.bso.notification.domain.notification.entity.Client;
import com.bso.notification.domain.notification.valueobject.NotificationConfiguration;
import io.micrometer.observation.Observation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.function.Predicate;

import static com.bso.notification.commons.mono.MonoUtils.monoError;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);
    private final ClientRepository clientRepository;
    private final NotificationGateway notificationGateway;
    private final NotificationMetricRegistry notificationMetricRegistry;
    private final ReactiveObservationHelper observationHelper;

    public Mono<Void> sendNotification(SendNotificationCommand command) {
        return observationHelper
            .runObserved("NotificationService.sendNotification", obs -> doSendNotification(command, obs));
    }

    private Mono<Void> doSendNotification(SendNotificationCommand command, Observation obs) {
        LOGGER.info(
            "Starting notification flow for client: {}, entity: {}, event: {}",
            command.clientId(),
            command.entity(),
            command.event()
        );
        return clientRepository
            .findById(command.clientId().toString())
            .switchIfEmpty(handleClientNotFound(command.clientId()))
            .doOnNext(c -> obs.scoped(() -> LOGGER.info("Client '{}' found", command.clientId())))
            .flatMap(client -> obs.scoped(() -> {
                var cfgOpt = client.notificationConfigurations().stream()
                    .filter(filter(command))
                    .findFirst();
                if (cfgOpt.isEmpty()) {
                    return monoError(
                        "Client %s does not have configuration for %s/%s",
                        client.id(),
                        command.entity(),
                        command.event()
                    );
                }

                var cfg = cfgOpt.get();

                LOGGER.info(
                    "Sending notification. client: {}, entity: {}, event: {}",
                    command.clientId(),
                    command.entity(),
                    command.event()
                );
                return notificationGateway.send(cfg.url(), command.data());
            }))
            .then(notificationMetricRegistry.incrementNotificationCounter())
            .onErrorResume(ex -> obs.scoped(() -> {
                LOGGER.error("Error during notification", ex);
                return notificationMetricRegistry.incrementErrorCounter()
                    .then(Mono.error(ex));
            }))
            .doFinally(sig -> obs.stop());
    }

    private Predicate<NotificationConfiguration> filter(
        SendNotificationCommand command
    ) {
        return c -> c.entity() == command.entity() &&
            c.event() == command.event();
    }

    private Mono<Client> handleClientNotFound(UUID id) {
        return monoError("Client with id %s not found", id);
    }
}
