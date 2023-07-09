package com.bso.notification.web.controller.client.dto;

import java.util.List;

public record CreateClientRequest(
    String name,
    List<CreateClientWebhookRequest> webhooks
) {}

