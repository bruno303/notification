package com.bso.notification.infra.observability.tracing;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.application")
public record ApplicationProperties(
    String name,
    String env
) {}
