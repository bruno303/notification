package com.bso.notification.web.controller.client;

import com.bso.notification.application.client.command.CreateClientCommand;
import com.bso.notification.application.client.service.ClientService;
import com.bso.notification.domain.notification.entity.Client;
import com.bso.notification.domain.notification.enums.Entity;
import com.bso.notification.domain.notification.enums.Event;
import com.bso.notification.web.controller.client.dto.CreateClientRequest;
import com.bso.notification.web.controller.client.dto.CreateClientResponse;
import com.bso.notification.web.controller.client.dto.CreateClientWebhookResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @GetMapping
    public Flux<Client> findAll() {
        return clientService.findAll();
    }

    @GetMapping("{id}")
    public Mono<Client> findById(@PathVariable UUID id) {
        return clientService.findById(id);
    }

    @PostMapping
    public Mono<CreateClientResponse> create(@RequestBody CreateClientRequest request) {
        return clientService
            .create(toCommand(request))
            .map(this::toResponse);
    }

    private CreateClientCommand toCommand(CreateClientRequest request) {
        return new CreateClientCommand(
            request.name(),
            request.webhooks().stream().map(w ->
                new CreateClientCommand.CreateClientWebhook(
                    Entity.valueOf(w.entity()),
                    Event.valueOf(w.event()),
                    w.url()
                )
            ).toList()
        );
    }

    private CreateClientResponse toResponse(Client client) {
        return new CreateClientResponse(
            client.name(),
            client.notificationConfigurations().stream().map(c ->
                new CreateClientWebhookResponse(
                    c.entity(),
                    c.event(),
                    c.url()
                )
            ).toList()
        );
    }
}
