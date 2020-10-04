package com.events.api.controllers;

import com.events.api.payload.request.auth.Login;
import com.events.api.payload.request.auth.Signup;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void it_should_return_ok_created_user() throws Exception {
        Signup signup = new Signup("martha", "martha@gmail.com", "13246578");

        mockMvc.perform(post("/api/auth/signup")
                .content(objectMapper.writeValueAsString(signup))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void it_should_return_token_login() throws Exception {
        Login login = new Login("martha", "13246578");

        mockMvc.perform(post("/api/auth/signin")
                .content(objectMapper.writeValueAsString(login))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }
}
