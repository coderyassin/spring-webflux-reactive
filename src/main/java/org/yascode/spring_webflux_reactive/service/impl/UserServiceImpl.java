package org.yascode.spring_webflux_reactive.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.yascode.spring_webflux_reactive.entity.User;
import org.yascode.spring_webflux_reactive.exception.ResourceNotFoundException;
import org.yascode.spring_webflux_reactive.repository.UserRepository;
import org.yascode.spring_webflux_reactive.service.UserService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.BiPredicate;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final List<String> validNames = List.of("b3Ci3M", "sJHKKt", "z9jRs1");

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Flux<User> getAllUsers() {
        return userRepository.findAll()
                .filter(user -> nameInList(validNames, user.getName()))
                .map(user -> {
                    user.setName("__" + user.getName());
                    return user;
                })
                .doOnNext(user -> log.info("==> {}", user.toString()))
                .doOnError(error -> log.error(error.getMessage()))
                .doOnComplete(() -> log.info("All users have been loaded"));
    }

    @Override
    public Mono<User> getUserById(Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setName("**" + user.getName());
                    return user;
                })
                .doOnTerminate(() -> log.info("Action complete"))
                .doOnSuccess(user -> log.info("The user with id {} has been recovered", user.getId()));
    }

    @Override
    public Flux<User> getUsersByName(String name) {
        return userRepository.findByNameContaining(name);
    }

    @Override
    public Mono<User> registerUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public Mono<User> retrieveUserByName(String name) {
        Mono<User> userMono = userRepository.findAll()
                .filter(user -> user.getName().equals(name))
                .next()
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("User not found")));
        return userMono;

    }

    private boolean nameInList(List<String> validNames, String name) {
        BiPredicate<List<String>, String> nameInList = List::contains;
        return nameInList.test(validNames, name);
    }
}
