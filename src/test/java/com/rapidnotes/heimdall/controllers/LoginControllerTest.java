package com.rapidnotes.heimdall.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rapidnotes.heimdall.dao.UserRepo;
import com.rapidnotes.heimdall.domain.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class LoginControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepo repo;
    @Autowired
    PasswordEncoder encoder;

    @BeforeEach
    void setUp() {
        addTestDataToDatabase(repo);
    }

    @AfterEach
    void tearDown() {
        repo.deleteAll();
    }

    @Test
    void login() throws Exception {
        Map<String, String> body = new HashMap<>();
        body.put("username", "jdoe");
        body.put("password", "testPass1");


        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(post("/api/v1/login")
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("jdoe"))
                .andExpect(jsonPath("$.firstname").value("john"))
                .andExpect(jsonPath("$.lastname").value("doe"))
                .andExpect(jsonPath("$.email").value("johnd@gmail.com"));
    }

    private void addTestDataToDatabase(UserRepo repo) {
        repo.save(new User("jdoe", "john", "doe",
                "johnd@gmail.com", encoder.encode("testPass1")));
    }
}