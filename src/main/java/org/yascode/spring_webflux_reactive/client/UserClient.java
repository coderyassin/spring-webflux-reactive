package org.yascode.spring_webflux_reactive.client;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.yascode.spring_webflux_reactive.client.api.UserClientApi;
import org.yascode.spring_webflux_reactive.entity.User;
import reactor.core.publisher.Mono;

import static org.yascode.spring_webflux_reactive.client.routes.ApiPaths.USERS_BY_NAME;

@Service
public class UserClient implements UserClientApi {
    private final WebClient webClient;

    public UserClient(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Mono<Object> retrieveUserByName(String name) {
        return webClient.get()
                .uri(USERS_BY_NAME, name)
                .retrieve()
                .bodyToMono(User.class)
                .map(this::rebuildUser);
    }

    @Override
    public Mono<ResponseEntity<?>> retrieveUserByNameV2(String name) {
        return webClient.get()
                .uri(USERS_BY_NAME, name)
                .exchangeToMono(clientResponse -> {
                    if (clientResponse.statusCode().is2xxSuccessful()) {
                        return clientResponse.bodyToMono(User.class)
                                .map(user -> ResponseEntity.ok()
                                        .headers(clientResponse.headers().asHttpHeaders())
                                        .body(user));
                    }
                    return clientResponse.bodyToMono(Object.class)
                            .map(error ->
                                    ResponseEntity.status(clientResponse.statusCode())
                                            .headers(clientResponse.headers().asHttpHeaders())
                                            .body(error)
                            );
                });
    }

    private User rebuildUser(User user) {
        user.setName("__" + user.getName());
        return user;
    }
}
