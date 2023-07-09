package com.bso.notification.infra.database;

import com.bso.notification.application.client.repository.ClientRepository;
import com.bso.notification.domain.notification.entity.Client;
import com.bso.notification.domain.notification.enums.Entity;
import com.bso.notification.domain.notification.enums.Event;
import com.bso.notification.domain.notification.valueobject.NotificationConfiguration;
import com.mongodb.ConnectionString;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Stream;

@EnableReactiveMongoRepositories
@Configuration(proxyBeanMethods = false)
public class MongoDbConfig extends AbstractReactiveMongoConfiguration {
    private final ConnectionString connectionString;

    public MongoDbConfig(
        @Value("${app.datasource.mongodb.url}") String connectionString
    ) {
        this.connectionString = new ConnectionString(connectionString);
    }

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create(connectionString.getConnectionString());
    }

    @Override
    protected String getDatabaseName() {
        return connectionString.getDatabase();
    }

    @Override
    public MongoClient reactiveMongoClient() {
        return mongoClient();
    }

    @Bean
    public CommandLineRunner testDataInDatabase(ClientRepository clientRepository) {
        return args -> {
            var client = new Client(
                UUID.randomUUID().toString(),
                "Company X",
                Stream.of(new NotificationConfiguration(
                    Entity.PAYMENT,
                    Event.CREATED,
                    "http://localhost:3000/payment/created",
                    LocalDateTime.now(),
                    null
                )).toList(),
                LocalDateTime.now(),
                null
            );
            clientRepository.deleteAll()
                .then(clientRepository.save(client))
                .block();
        };
    }
}
