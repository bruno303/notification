package com.bso.notification.web.controller.notification.dto;

public record SendNotificationRequest(
    String clientId,
    String entity,
    String event,
    String data
) {}
