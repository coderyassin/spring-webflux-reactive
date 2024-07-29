package org.yascode.spring_webflux_reactive.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class NameRequest {
    private String name;
}
