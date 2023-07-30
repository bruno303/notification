package com.bso.notification.infra.sqs;

import com.bso.notification.async.MessageHandler;
import com.bso.notification.commons.mono.MonoUtils;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.time.Duration;

public class SqsListener {
    private final Logger LOGGER = LoggerFactory.getLogger(SqsListener.class);

    private final SqsAsyncClient sqsAsyncClient;
    private final MessageHandler handler;
    private final String queueUrl;
    private final String queueName;
    private Boolean active = true;

    public SqsListener(SqsAsyncClient sqsAsyncClient, MessageHandler handler, String queueName) {
        this.sqsAsyncClient = sqsAsyncClient;
        this.handler = handler;
        this.queueName = queueName;
        this.queueUrl = getQueueUrl(queueName);
    }

    public void listen() {
        LOGGER.info("Starting listener for queue {}", queueName);
        launchProducer();
    }

    private void launchProducer() {
        var request = ReceiveMessageRequest.builder()
            .queueUrl(queueUrl)
            .maxNumberOfMessages(10)
            .build();

        Mono.fromFuture(() -> sqsAsyncClient.receiveMessage(request))
            .publishOn(Schedulers.single())
            .doOnNext(response -> LOGGER.debug("Received {} messages", response.messages().size()))
            .map(ReceiveMessageResponse::messages)
            .flatMapMany(messages -> Flux.fromIterable(messages)
                .concatWith(
                    MonoUtils.applyDelay(Duration.ofSeconds(1))
                )
            )
            .publishOn(Schedulers.boundedElastic())
            .flatMap(message ->
                this.process(message)
                    .then(deleteMessage(message))
                    .onErrorResume(ex -> {
                        // TODO implement redrive on error
                        LOGGER.error(String.format("Failed to receive messages from queue %s", queueUrl), ex);
                        return Mono.empty();
                    })
            )
            .doOnComplete(() -> LOGGER.debug("Messages processed"))
            .repeat(() -> active)
            .subscribe();
    }

    public void cancel() {
        LOGGER.info("Stopping listener for queue {}", queueName);
        this.active = false;
    }

    private Mono<Void> process(Message message) {
        return handler.handle(message.body());
    }

    private Mono<Void> deleteMessage(Message message) {
        var request = DeleteMessageRequest.builder()
            .queueUrl(queueUrl)
            .receiptHandle(message.receiptHandle())
            .build();
        return Mono.fromFuture(sqsAsyncClient.deleteMessage(request)).then();
    }

    @SneakyThrows
    private String getQueueUrl(String queueName) {
        return sqsAsyncClient.getQueueUrl(
            GetQueueUrlRequest.builder().queueName(queueName).build()
        ).get().queueUrl();
    }
}
