package com.bso.notification.application.client.service;

import com.bso.notification.application.client.command.CreateClientCommand;
import com.bso.notification.application.client.repository.ClientRepository;
import com.bso.notification.domain.notification.entity.Client;
import com.bso.notification.domain.notification.valueobject.NotificationConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;

    public Flux<Client> findAll() {
        return clientRepository.findAll();
    }

    public Mono<Client> findById(UUID id) {
        return clientRepository.findById(id.toString());
    }

    public Mono<Client> create(CreateClientCommand command) {
        return clientRepository.save(toClient(command));
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
