package com.bso.notification.infra.sqs.config;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.providers.DefaultAwsRegionProviderChain;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import java.net.URI;

@Configuration
public class SqsConfiguration {
    private final Logger LOGGER = LoggerFactory.getLogger(SqsConfiguration.class);

    @Bean
    public SqsAsyncClient sqsAsyncClient(
        @Value("${app.aws.sqs.endpoint}") String sqsEndpoint
    ) {
        return SqsAsyncClient.builder()
            .region(new DefaultAwsRegionProviderChain().getRegion())
            .endpointOverride(toURI(sqsEndpoint))
            .credentialsProvider(DefaultCredentialsProvider.create())
            .build();
    }

    @SneakyThrows
    private URI toURI(String str) {
        return new URI(str);
    }
}
