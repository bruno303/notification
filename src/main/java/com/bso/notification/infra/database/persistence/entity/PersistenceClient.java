package com.bso.notification.infra.database.persistence.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "client")
public record PersistenceClient(
    String id,
    String name,
    List<PersistenceNotificationConfiguration> notificationConfigurations,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public record PersistenceNotificationConfiguration(
        String entity,
        String event,
        String url,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) { }
}

