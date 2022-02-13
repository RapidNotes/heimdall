package com.rapidnotes.heimdall.config;

import com.rapidnotes.heimdall.util.JWTTokenUtil;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

@Configuration
@ActiveProfiles("test")
public class TestConfig {

    @Bean
    @Primary
    public PasswordEncoder testEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Primary
    public JWTTokenUtil jwtTokenUtil() {
        String jwtSecret = "jdahhddsfdsfytyvbczxvfsdiuwerdsfsdhfgsdhgewruewsdaksdjshasjfkgdshfgsdfuewyge";
        return new JWTTokenUtil(jwtSecret, new SecretKeySpec(DatatypeConverter.parseBase64Binary(jwtSecret),
                SignatureAlgorithm.HS512.getJcaName()), "rapid-notes.com");
    }
}
