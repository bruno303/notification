package com.bso.notification.application.notification.service;

import com.bso.notification.application.client.repository.ClientRepository;
import com.bso.notification.application.notification.command.SendNotificationCommand;
import com.bso.notification.application.notification.gateway.NotificationGateway;
import com.bso.notification.domain.notification.entity.Client;
import com.bso.notification.domain.notification.valueobject.NotificationConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final ClientRepository clientRepository;
    private final NotificationGateway notificationGateway;

    public Mono<Void> sendNotification(SendNotificationCommand command) {
        return clientRepository
            .findById(command.clientId().toString())
            .switchIfEmpty(handleClientNotFound(command.clientId()))
            .flatMap(client -> {
                var cfgOpt = client.notificationConfigurations().stream()
                        .filter(filter(command))
                        .findFirst();
                if (cfgOpt.isEmpty()) {
                    return Mono.error(
                        new IllegalStateException(
                            String.format(
                                "Client %s does not have configuration for %s/%s",
                                client.id(),
                                command.entity(),
                                command.event()
                            )
                        )
                    );
                }

                var cfg = cfgOpt.get();
                return notificationGateway.send(cfg.url(), command.data());
            });
    }

    private Predicate<NotificationConfiguration> filter(
        SendNotificationCommand command
    ) {
        return c -> c.entity() == command.entity() &&
            c.event() == command.event();
    }

    private Mono<Client> handleClientNotFound(UUID id) {
        return Mono.error(
            new IllegalStateException(String.format("Client with id %s not found", id.toString()))
        );
    }
}
