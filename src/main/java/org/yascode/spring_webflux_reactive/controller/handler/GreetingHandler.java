package org.yascode.spring_webflux_reactive.controller.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.yascode.spring_webflux_reactive.model.NameRequest;
import reactor.core.publisher.Mono;

@Component
public class GreetingHandler {
    public Mono<ServerResponse> greetingGet(ServerRequest request) {
        String name = request.queryParam("name").orElse("Guest");
        String responseMessage = "Hello, " + name + "!";
        return ServerResponse.ok()
                .body(Mono.just(responseMessage), String.class);
    }

    public Mono<ServerResponse> greetingPost(ServerRequest request) {
        return request.bodyToMono(NameRequest.class)
                .flatMap(nameRequest -> {
                    String responseMessage = "Hello, " + nameRequest.getName() + "!";
                    return ServerResponse.ok().body(Mono.just(responseMessage), String.class);
                });
    }
}
