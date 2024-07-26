package org.yascode.spring_webflux_reactive.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.yascode.spring_webflux_reactive.entity.User;
import reactor.core.publisher.Flux;

public interface UserRepository extends ReactiveCrudRepository<User, Long> {
    Flux<User> findByNameContaining(String name);
}
