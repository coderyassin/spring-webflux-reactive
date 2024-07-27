package org.yascode.spring_webflux_reactive.controller.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorMessage {
    private int status;
    private LocalDateTime timestamp;
    private String message;
    private String path;
}
