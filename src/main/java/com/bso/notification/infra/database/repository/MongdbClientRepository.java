package com.bso.notification.infra.database.repository;

import com.bso.notification.application.client.repository.ClientRepository;
import com.bso.notification.domain.notification.entity.Client;
import com.bso.notification.domain.notification.enums.Entity;
import com.bso.notification.domain.notification.enums.Event;
import com.bso.notification.domain.notification.valueobject.NotificationConfiguration;
import com.bso.notification.infra.database.persistence.entity.PersistenceClient;
import com.bso.notification.infra.database.persistence.repository.MongoDbClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MongdbClientRepository implements ClientRepository {
    private final MongoDbClientRepository mongoRepository;

    @Override
    public Mono<Client> findById(String id) {
        return mongoRepository
            .findById(id)
            .map(this::toDomain);
    }

    @Override
    public Mono<Client> save(Client client) {
        var persistenceClient = toPersistence(client);
        return mongoRepository
            .save(persistenceClient)
            .map(this::toDomain);
    }

    @Override
    public Flux<Client> findAll() {
        return mongoRepository
            .findAll()
            .map(this::toDomain);
    }

    @Override
    public Mono<Void> deleteAll() {
        return mongoRepository.deleteAll();
    }

    private PersistenceClient toPersistence(Client client) {
        return new PersistenceClient(
            client.id(),
            client.name(),
            toPersistence(client.notificationConfigurations()),
            client.createdAt(),
            client.updatedAt()
        );
    }

    private Client toDomain(PersistenceClient persistence) {
        return new Client(
            persistence.id(),
            persistence.name(),
            toDomain(persistence.notificationConfigurations()),
            persistence.createdAt(),
            persistence.updatedAt()
        );
    }

    private List<PersistenceClient.PersistenceNotificationConfiguration> toPersistence(
        List<NotificationConfiguration> source
    ) {
        return source.stream()
            .map(s ->
                new PersistenceClient.PersistenceNotificationConfiguration(
                    s.entity().name(),
                    s.event().name(),
                    s.url(),
                    s.createdAt(),
                    s.updatedAt()
                )
            ).toList();
    }

    private List<NotificationConfiguration> toDomain(
        List<PersistenceClient.PersistenceNotificationConfiguration> source
    ) {
        return source.stream()
            .map(s ->
                new NotificationConfiguration(
                    Entity.valueOf(s.entity()),
                    Event.valueOf(s.event()),
                    s.url(),
                    s.createdAt(),
                    s.updatedAt()
                )
            ).toList();
    }
}
