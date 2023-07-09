package com.bso.notification.web.controller.client.dto;

import java.util.List;

public record CreateClientResponse(
    String name,
    List<CreateClientWebhookResponse> webhooks
) {}

