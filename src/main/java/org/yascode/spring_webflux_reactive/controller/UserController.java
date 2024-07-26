package org.yascode.spring_webflux_reactive.controller;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yascode.spring_webflux_reactive.entity.User;
import org.yascode.spring_webflux_reactive.service.UserService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Flux<User> getAllUsers() {
        /*List<User> users = new ArrayList<>();
        userService.getAllUsers()
                .subscribe(
                        user -> {
                            log.info("Recue : " + user);
                            users.add(user);
                        },
                        error -> log.error("Error: " + error.getMessage()),
                        () -> log.info("Terminé")
                );*/

        return userService.getAllUsers()
                .doOnNext(user -> log.info("Received: " + user))
                /*.collectList()
                .flatMapMany(users -> Flux.fromIterable(users))*/
                .doOnError(error -> log.error("Error : " + error.getMessage()))
                .doOnComplete(() -> log.info("Completed"));
    }

    @GetMapping("/{id}")
    public Mono<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }
}
