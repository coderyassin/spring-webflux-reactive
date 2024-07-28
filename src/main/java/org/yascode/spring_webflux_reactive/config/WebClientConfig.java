package org.yascode.spring_webflux_reactive.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
public class WebClientConfig {
    @Value("${server.port}")
    private String serverPort;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("http://" + getLocalIpAddress() + ":" + serverPort)
                .defaultHeaders(httpHeaders -> httpHeaders.add("Content-Type", "application/json"))
                .build();
    }

    private String getLocalIpAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return "localhost";
        }
    }
}
