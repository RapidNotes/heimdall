package com.rapidnotes.heimdall.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.rapidnotes.heimdall.dao.UserRepo;
import com.rapidnotes.heimdall.domain.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class RegisterControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepo repo;

    @BeforeEach
    void setUp() {
        addTestDataToDatabase(repo);
    }

    @AfterEach
    void tearDown() {
        repo.deleteAll();
    }

    @Test
    @DisplayName("POST /api/v1/register happy path test")
    void registerUser() throws Exception {
        Map<String, String> body = new HashMap<>();
        body.put("username", "test_user");
        body.put("firstname", "test_firstname");
        body.put("lastname", "test_lastname");
        body.put("password", "test_password");
        body.put("email", "test_email");

        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(post("/api/v1/register").content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("test_user"))
                .andExpect(jsonPath("$.firstname").value("test_firstname"))
                .andExpect(jsonPath("$.lastname").value("test_lastname"))
                .andExpect(jsonPath("$.email").value("test_email"))
                .andExpect(jsonPath("$.*", hasSize(4)));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/v1/register/{username} happy path test")
    void getUser() throws Exception {
        mockMvc.perform(get("/api/v1/register/jdoe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("jdoe"))
                .andExpect(jsonPath("$.firstname").value("john"))
                .andExpect(jsonPath("$.lastname").value("doe"))
                .andExpect(jsonPath("$.email").value("johnd@gmail.com"))
                .andExpect(jsonPath("$.*", hasSize(4)));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/v1/register happy path test")
    void getAllUsers() throws Exception {
        mockMvc.perform(get("/api/v1/register"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].*", hasSize(4)))
                .andExpect(jsonPath("$[1].*", hasSize(4)));
    }

    private void addTestDataToDatabase(UserRepo repo) {
        repo.save(new User("jdoe", "john", "doe",
                "johnd@gmail.com", "testPass1"));
        repo.save(new User("edonnar", "Erdir", "Donnar",
                "edonnar@gmail.com", "testPass2"));
    }
}
