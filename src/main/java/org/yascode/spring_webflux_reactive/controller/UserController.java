package org.yascode.spring_webflux_reactive.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import org.yascode.spring_webflux_reactive.entity.User;
import org.yascode.spring_webflux_reactive.service.UserService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getAllUsers());
    }

    @GetMapping("/flux")
    public Flux<User> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getUserById(id));
    }

    @GetMapping(value = "/byName/{name}")
    ResponseEntity<?> retrieveUserByName(@PathVariable(name = "name") String name, ServerWebExchange serverWebExchange) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.retrieveUserByName(name));
    }

    @GetMapping(value = "/byNameV2/{name}")
    Mono<ResponseEntity<?>> retrieveUserByNameV2(@PathVariable(name = "name") String name, ServerWebExchange serverWebExchange) {
        return userService.retrieveUserByNameV2(name);
    }

    @PostMapping
    ResponseEntity<?> registerUser(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.registerUser(user));
    }

    @GetMapping(value = "/deleteDuplicates/{name}")
    public Flux<User> deleteDuplicates(@PathVariable("name") String name) {
        return userService.deleteDuplicates(name);
    }
}
