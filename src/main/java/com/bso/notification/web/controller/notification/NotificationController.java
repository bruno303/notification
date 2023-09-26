package com.bso.notification.web.controller.notification;

import com.bso.notification.application.notification.command.SendNotificationCommand;
import com.bso.notification.application.notification.service.NotificationService;
import com.bso.notification.domain.notification.enums.Entity;
import com.bso.notification.domain.notification.enums.Event;
import com.bso.notification.web.controller.notification.dto.SendNotificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping
    public Mono<Void> sendNotification(@RequestBody SendNotificationRequest body) {
        return notificationService.sendNotification(toCommand(body));
    }

    private SendNotificationCommand toCommand(SendNotificationRequest source) {
        return new SendNotificationCommand(
            UUID.fromString(source.clientId()),
            Entity.valueOf(source.entity()),
            Event.valueOf(source.event()),
            source.data()
        );
    }
}
