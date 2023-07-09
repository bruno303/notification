package com.bso.notification.domain.notification.entity;

import com.bso.notification.domain.notification.valueobject.NotificationConfiguration;

import java.time.LocalDateTime;
import java.util.List;

public record Client(
    String id,
    String name,
    List<NotificationConfiguration> notificationConfigurations,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
