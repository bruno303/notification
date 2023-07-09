package com.bso.notification.application.client.command;

import com.bso.notification.domain.notification.enums.Entity;
import com.bso.notification.domain.notification.enums.Event;

import java.util.List;

public record CreateClientCommand(
    String name,
    List<CreateClientWebhook> clientConfigurations
) {
    public record CreateClientWebhook(
        Entity entity,
        Event event,
        String url
    ) { }
}

