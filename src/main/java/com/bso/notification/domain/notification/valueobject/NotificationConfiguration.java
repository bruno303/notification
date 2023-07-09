package com.bso.notification.domain.notification.valueobject;

import com.bso.notification.domain.notification.enums.Entity;
import com.bso.notification.domain.notification.enums.Event;

import java.time.LocalDateTime;

public record NotificationConfiguration(
    Entity entity,
    Event event,
    String url,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) { }
