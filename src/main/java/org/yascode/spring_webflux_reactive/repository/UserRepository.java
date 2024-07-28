package org.yascode.spring_webflux_reactive.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.yascode.spring_webflux_reactive.entity.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, Long> {
    Flux<User> findByNameContaining(String name);

    Flux<User> findByName(String name);

    @Query("SELECT COUNT(*) FROM users WHERE name = :name")
    Mono<Long> countUsersByName(String name);


}
