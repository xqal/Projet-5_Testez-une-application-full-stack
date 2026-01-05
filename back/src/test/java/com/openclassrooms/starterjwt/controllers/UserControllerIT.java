package com.openclassrooms.starterjwt.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.test.context.TestPropertySource;
import org.springframework.security.test.context.support.WithMockUser;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;



@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")
@Transactional
public class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    // -- GET FIND BY ID
    @Test
    @WithMockUser(username = "user@user.com")
    public void findById_ReturnUser() throws Exception {
        // ARRANGE    
        User user = new User();
        user.setEmail("user@user.com");
        user.setFirstName("First_User");
        user.setLastName("Last_User");
        user.setPassword("password");
        user.setAdmin(false);

        user = userRepository.save(user);

        // ACT
        mockMvc.perform(get("/api/user/" + user.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value("user@user.com"))
            .andExpect(jsonPath("$.firstName").value("First_User"))
            .andExpect(jsonPath("$.lastName").value("Last_User"));

    }

    @Test
    @WithMockUser
    public void findById_UserNotFound() throws Exception {
        // ASSERT
        mockMvc.perform(get("/api/user/100"))
                .andExpect(status().isNotFound());
    }


    // -- DELETE
    @Test
    @WithMockUser(username = "test@delete.com")
    public void delete_DeleteUser() throws Exception {

        // ARRANGE
        User user = new User();
        user.setEmail("test@delete.com");
        user.setLastName("Last_Delete");
        user.setFirstName("First_Delete");
        user.setPassword("password");
        user.setAdmin(false);

        user = userRepository.save(user);

        // ACT
        mockMvc.perform(delete("/api/user/" + user.getId()))
                .andExpect(status().isOk());

        // ASSERT
        assertThat(userRepository.findById(user.getId())).isEmpty();
    }

    @Test
    @WithMockUser
    public void delete_UserNotFound() throws Exception {
        // ASSERT
        mockMvc.perform(delete("/api/user/100"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "unauthorized@unauthorized.com")
    public void delete_UnauthorizedUser() throws Exception {
        //ARRANGE
        User user = new User();
        user.setEmail("owner@owner.com");
        user.setFirstName("First_Owner");
        user.setLastName("Last_Owner");
        user.setPassword("password");
        user.setAdmin(false);

        user = userRepository.save(user);
        
        // ASSERT
        mockMvc.perform(delete("/api/user/" + user.getId()))
                .andExpect(status().isUnauthorized());
    }
}
