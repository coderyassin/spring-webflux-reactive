package org.yascode.spring_webflux_reactive.service.impl;

import org.springframework.stereotype.Service;
import org.yascode.spring_webflux_reactive.entity.User;
import org.yascode.spring_webflux_reactive.repository.UserRepository;
import org.yascode.spring_webflux_reactive.service.UserService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Flux<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Mono<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Flux<User> getUsersByName(String name) {
        return userRepository.findByNameContaining(name);
    }
}
