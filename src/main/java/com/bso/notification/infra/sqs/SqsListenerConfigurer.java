package com.bso.notification.infra.sqs;

import com.bso.notification.async.MessageHandler;
import com.bso.notification.infra.sqs.config.AppSqsConfigurationProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@Component
@RequiredArgsConstructor
public class SqsListenerConfigurer {
    private final ApplicationContext context;
    private final SqsAsyncClient sqsAsyncClient;
    private final AppSqsConfigurationProperties appSqsConfigurationProperties;

    @PostConstruct
    public void configure() {
        var configs = appSqsConfigurationProperties.configs();
        if (configs == null) return;

        configs.stream()
            .map(cfg -> {
                var handler = context.getBean(cfg.handler(), MessageHandler.class);
                return new SqsListener(
                    sqsAsyncClient,
                    handler,
                    cfg.queue()
                );
            })
            .toList()
            .forEach(SqsListener::listen);
    }
}
