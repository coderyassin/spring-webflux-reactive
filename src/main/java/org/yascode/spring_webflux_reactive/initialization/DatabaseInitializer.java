package org.yascode.spring_webflux_reactive.initialization;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yascode.spring_webflux_reactive.entity.User;
import org.yascode.spring_webflux_reactive.repository.UserRepository;
import reactor.core.publisher.Flux;

import java.util.Random;

@Configuration
@Slf4j
public class DatabaseInitializer {

    //@Bean
    CommandLineRunner initDatabase(UserRepository userRepository) {
        return args -> Flux.range(0, 100)
            .map(i -> createUser())
            .flatMap(user -> userRepository.save(user))
            .doOnComplete(() -> log.info("Database initialization completed"))
            .doOnError(e -> log.error("Database initialization failed", e))
            .subscribe()
            .dispose();
    }

    private User createUser() {
        String name = generateName();
        return User.builder()
                .name(name)
                .email(generateEmail(name))
                .build();
    }

    private String generateName() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(6);
        Random random = new Random();

        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }

        return sb.toString();
    }

    private String generateEmail(String name) {
        return new StringBuilder(name).append("@gmail.com").toString();
    }

}
