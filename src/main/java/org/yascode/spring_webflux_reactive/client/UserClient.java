package org.yascode.spring_webflux_reactive.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.yascode.spring_webflux_reactive.client.api.UserClientApi;
import org.yascode.spring_webflux_reactive.entity.User;
import org.yascode.spring_webflux_reactive.repository.UserRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import static org.yascode.spring_webflux_reactive.client.routes.ApiPaths.USERS_BY_NAME;
import static org.yascode.spring_webflux_reactive.client.routes.ApiPaths.USERS_FLUX;

@Service
@Slf4j
public class UserClient implements UserClientApi {
    private final String prefix = "__";
    private final WebClient webClient;
    private final UserRepository userRepository;

    public UserClient(WebClient webClient,
                      UserRepository userRepository) {
        this.webClient = webClient;
        this.userRepository = userRepository;
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

    @Override
    public Flux<User> deleteDuplicates(String name) {
        AtomicReference<Boolean> alreadySeen = new AtomicReference<>(false);
        return webClient.get()
                .uri(USERS_FLUX)
                .retrieve()
                .bodyToFlux(User.class)
                .flatMap(user -> userRepository.countUsersByName(name)
                        .flatMap(count -> {
                            if(count > 1) {
                                if(alreadySeen.get()) {
                                    return Mono.empty();
                                }
                                return userRepository.findByName(name)
                                        .flatMap(u -> {
                                            if(!alreadySeen.get()) {
                                                alreadySeen.set(true);
                                                return userRepository.delete(u).thenReturn(u);
                                            }
                                            return Mono.empty();
                                        })
                                        .next();
                            }
                            return Mono.empty();
                        })
                );
    }

    //@Override
    public Flux<User> deleteDuplicates(String name, int a) {
        return userRepository.findByName(name)
                .groupBy(User::getName)
                .flatMap(group -> group.skip(1))
                .flatMap(user -> userRepository.deleteById(user.getId()).thenReturn(user));
    }

    private User rebuildUser(User user) {
        user.setName(prefix + user.getName());
        return user;
    }
}
