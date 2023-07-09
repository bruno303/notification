package com.bso.notification.application.notification.command;

import com.bso.notification.domain.notification.enums.Entity;
import com.bso.notification.domain.notification.enums.Event;

import java.util.UUID;

public record SendNotificationCommand(
    UUID clientId,
    Entity entity,
    Event event,
    String data
) {}
