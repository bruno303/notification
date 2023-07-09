package com.bso.notification.infra.sqs.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "app.aws.sqs")
public record AppSqsConfigurationProperties(
    List<ConfigItem> configs
) {
    public record ConfigItem(
        String queue,
        String handler
    ) {}
}
