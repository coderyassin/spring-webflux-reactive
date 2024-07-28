package org.yascode.spring_webflux_reactive.client.api;

import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface UserClientApi {
    Mono<Object> retrieveUserByName(String name);
    Mono<ResponseEntity<?>> retrieveUserByNameV2(String name);

}
