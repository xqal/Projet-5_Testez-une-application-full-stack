package com.openclassrooms.starterjwt.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.context.TestPropertySource;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")
@Transactional

public class AuthControllerIT {
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void authenticateUser_Login() throws Exception {

        User user = new User();
        user.setEmail("user@user.com");
        user.setFirstName("First_User");
        user.setLastName("Last_User");
        user.setPassword(passwordEncoder.encode("password"));
        user.setAdmin(false);
        user = userRepository.save(user);

        String loginBody = "{\"email\": \"user@user.com\", \"password\": \"password\"}";

        mockMvc.perform(post("/api/auth/login")
            .contentType("application/json")
            .content(loginBody))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").exists())
            .andExpect(jsonPath("$.id").value(user.getId()))
            .andExpect(jsonPath("$.username").value("user@user.com"));
    }

    @Test
    public void registerUser_register() throws Exception {
        String registerBody = "{\"email\": \"newuser@user.com\", \"password\": \"password\", \"firstName\": \"First\", \"lastName\": \"Last\"}";

        mockMvc.perform(post("/api/auth/register")
            .contentType("application/json")
            .content(registerBody))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("User registered successfully!"));
            
        User user = userRepository.findByEmail("newuser@user.com").orElse(null);
        assertThat(user).isNotNull();        
    }

    @Test
    public void authenticateUser_UserAlreadyRegistered() throws Exception {
        User user = new User();
        user.setEmail("user@user.com");
        user.setFirstName("First_User");
        user.setLastName("Last_User");
        user.setPassword(passwordEncoder.encode("password"));
        user.setAdmin(false);
        userRepository.save(user);

        String registerBody = "{\"email\": \"user@user.com\", \"password\": \"password\", \"firstName\": \"First\", \"lastName\": \"Last\"}";

        mockMvc.perform(post("/api/auth/register")
            .contentType("application/json")
            .content(registerBody))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Error: Email is already taken!"));
        
        User existingUser = userRepository.findByEmail("user@user.com").orElse(null);
        assertThat(existingUser).isNotNull();
    }
}
