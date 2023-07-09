package com.bso.notification.application.client.repository;

import com.bso.notification.domain.notification.entity.Client;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ClientRepository {
    Mono<Client> findById(String id);
    Mono<Client> save(Client client);
    Flux<Client> findAll();
    Mono<Void> deleteAll();
}
