package org.yascode.spring_webflux_reactive.controller.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import org.yascode.spring_webflux_reactive.controller.response.ErrorMessage;
import org.yascode.spring_webflux_reactive.exception.ResourceNotFoundException;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = {ResourceNotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public Mono<ResponseEntity<ErrorMessage>> resourceNotFoundException(ResourceNotFoundException ex, ServerWebExchange serverWebExchange) {

        ErrorMessage errorMessage = ErrorMessage.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .timestamp(LocalDateTime.now())
                .message(ex.getMessage())
                .path(serverWebExchange.getRequest().getPath().value())
                .build();

        return Mono.justOrEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(errorMessage));
    }
}
