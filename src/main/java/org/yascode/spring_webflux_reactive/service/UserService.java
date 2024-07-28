package org.yascode.spring_webflux_reactive.service;

import org.springframework.http.ResponseEntity;
import org.yascode.spring_webflux_reactive.entity.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
    Flux<User> getAllUsers();

    Mono<User> getUserById(Long id);

    Flux<User> getUsersByName(String name);

    Mono<User> registerUser(User user);

    Mono<User> retrieveUserByName(String name);

    Mono<ResponseEntity<?>> retrieveUserByNameV2(String name);
}
