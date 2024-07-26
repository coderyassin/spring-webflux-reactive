package org.yascode.spring_webflux_reactive.service;

import org.yascode.spring_webflux_reactive.entity.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
    Flux<User> getAllUsers();

    Mono<User> getUserById(Long id);

    Flux<User> getUsersByName(String name);
}
