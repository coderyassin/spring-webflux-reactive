package org.yascode.spring_webflux_reactive.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.yascode.spring_webflux_reactive.client.api.UserClientApi;
import org.yascode.spring_webflux_reactive.entity.User;
import org.yascode.spring_webflux_reactive.exception.MultipleResourcesFoundException;
import org.yascode.spring_webflux_reactive.exception.ResourceNotFoundException;
import org.yascode.spring_webflux_reactive.repository.UserRepository;
import org.yascode.spring_webflux_reactive.service.UserService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.BiPredicate;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserClientApi userClientApi;
    private final UserRepository userRepository;
    private final List<String> validNames = List.of("b3Ci3M", "sJHKKt", "z9jRs1");

    public UserServiceImpl(UserClientApi userClientApi,
                           UserRepository userRepository) {
        this.userClientApi = userClientApi;
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
        return userRepository.findAll()
                .doOnNext(user -> log.info("Found user: {}", user))
                .flatMap(user -> {
                    if (user.getName().equals(name)) {
                        log.info("User matches: {}", user.getName());
                        return Mono.just(user);
                    } else {
                        log.info("User does not match: {}", user.getName());
                        return Mono.empty();
                    }
                })
                .single()
                .onErrorResume(fallback -> {
                    if (fallback instanceof NoSuchElementException) {
                        return Mono.error(new ResourceNotFoundException("User not found"));
                    } else if (fallback instanceof IndexOutOfBoundsException) {
                        return Mono.error(new MultipleResourcesFoundException("Multiple users found"));
                    }
                    return Mono.error(fallback);
                });
    }

    public Mono<ResponseEntity<?>> retrieveUserByNameV2(String name) {
        return userClientApi.retrieveUserByNameV2(name);
    }

    private boolean nameInList(List<String> validNames, String name) {
        BiPredicate<List<String>, String> nameInList = List::contains;
        return nameInList.test(validNames, name);
    }
}
