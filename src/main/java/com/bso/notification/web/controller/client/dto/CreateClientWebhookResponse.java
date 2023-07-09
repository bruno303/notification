package com.bso.notification.web.controller.client.dto;

import com.bso.notification.domain.notification.enums.Entity;
import com.bso.notification.domain.notification.enums.Event;

public record CreateClientWebhookResponse(
    Entity entity,
    Event event,
    String url
) {
}
