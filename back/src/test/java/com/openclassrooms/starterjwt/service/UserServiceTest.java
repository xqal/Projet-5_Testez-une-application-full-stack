package com.openclassrooms.starterjwt.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;

import com.openclassrooms.starterjwt.services.UserService;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;



@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void findUserByIdShouldReturnUser(){
        // ARRANGE
        User user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");
        user.setFirstName("Test");
        user.setLastName("Test");
        user.setPassword("password");
        user.setAdmin(false);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // ACT
        User result = userService.findById(1L);
        
        // ASSERT
        assertEquals(user, result);
    }

    @Test
    public void deleteById() {
        userService.delete(1L);
        verify(userRepository).deleteById(1L);
    }

}
