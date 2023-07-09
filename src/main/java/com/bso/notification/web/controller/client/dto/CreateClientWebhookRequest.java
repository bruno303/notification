package com.bso.notification.web.controller.client.dto;

public record CreateClientWebhookRequest(
    String entity,
    String event,
    String url
) {
}
