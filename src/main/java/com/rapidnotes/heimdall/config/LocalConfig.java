package com.rapidnotes.heimdall.config;

import com.rapidnotes.heimdall.dao.UserRepo;
import com.rapidnotes.heimdall.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration
@Profile("Local")
public class LocalConfig {
    @Autowired
    private PasswordEncoder encoder;

    @Bean
    CommandLineRunner initDatabase(UserRepo repo) {
        return args -> {
            log.info("Preloading " + repo.save(new User("bilbo12", "Bilbo", "Babbins",
                    "bilbo12@gmail.com", encoder.encode("userpass"))));
            log.info("Preloading " + repo.save(new User("frodo123", "Frodo", "Babbins",
                     "frodo@gmail.com", encoder.encode("userpass12"))));
            log.info("Preloading " + repo.save(new User("shashwat183", "Shashwat", "Pragya",
                     "frodo@gmail.com", encoder.encode("userpass13"))));
        };
    }


}
