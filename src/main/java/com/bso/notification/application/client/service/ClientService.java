package com.bso.notification.application.client.service;

import com.bso.notification.application.client.command.CreateClientCommand;
import com.bso.notification.application.client.repository.ClientRepository;
import com.bso.notification.crosscutting.observability.ReactiveObservationHelper;
import com.bso.notification.domain.notification.entity.Client;
import com.bso.notification.domain.notification.valueobject.NotificationConfiguration;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientService.class);
    private final ClientRepository clientRepository;
    private final ReactiveObservationHelper observationHelper;

    public Flux<Client> findAll() {
        return observationHelper.runObservedMany("ClientService.findAll", obs -> {
            obs.scoped(() -> LOGGER.info("Searching all clients"));
            return clientRepository.findAll();
        });
    }

    public Mono<Client> findById(UUID id) {
        return observationHelper.runObserved("ClientService.findById", obs -> {
            obs.scoped(() -> LOGGER.info("Searching for client with id {}", id));
            return clientRepository.findById(id.toString());
        });
    }

    public Mono<Client> create(CreateClientCommand command) {
        return observationHelper.runObserved("ClientService.create", obs -> {
            obs.scoped(() -> LOGGER.info("Creating client with name [{}]", command.name()));
            return clientRepository.save(toClient(command));
        });
    }

    private Client toClient(CreateClientCommand cmd) {
        return new Client(
            UUID.randomUUID().toString(),
            cmd.name(),
            cmd.clientConfigurations().stream().map(cc ->
                new NotificationConfiguration(
                    cc.entity(),
                    cc.event(),
                    cc.url(),
                    LocalDateTime.now(),
                    null
                )
            ).toList(),
            LocalDateTime.now(),
            null
        );
    }
}
