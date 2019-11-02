package com.sda.springmvc.example;

import com.github.javafaker.Faker;
import com.sda.springmvc.example.entities.User;
import com.sda.springmvc.example.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class DevConfig {

    @Bean
    CommandLineRunner dbInitializer(UserRepository userRepository, Faker faker) {
        return args -> {
            User user = new User("Alice", "alice@gmail.com", "GB");
            userRepository.save(user);

            for (int i = 0; i < 10; i++) {
                user = new User(
                        faker.name().username(),
                        faker.internet().emailAddress(),
                        faker.country().countryCode2());

                userRepository.save(user);
            }

        };
    }

    @Bean
    Faker faker() {
        return new Faker();
    }
}
