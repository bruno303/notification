package com.bso.notification.infra.sqs;

import com.bso.notification.async.MessageHandler;
import com.bso.notification.infra.sqs.config.AppSqsConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

@Component
public class SqsListenerConfigurer {
    private final ApplicationContext context;
    private final SqsAsyncClient sqsAsyncClient;
    private final AppSqsConfigurationProperties appSqsConfigurationProperties;

    public SqsListenerConfigurer(ApplicationContext context, SqsAsyncClient sqsAsyncClient, AppSqsConfigurationProperties appSqsConfigurationProperties) {
        this.context = context;
        this.sqsAsyncClient = sqsAsyncClient;
        this.appSqsConfigurationProperties = appSqsConfigurationProperties;
        CompletableFuture.runAsync(this::configure, Executors.newSingleThreadExecutor());
    }

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
            .forEach(listener -> {
                Runtime.getRuntime().addShutdownHook(new Thread(listener::cancel));
                listener.listen();
            });
    }
}
