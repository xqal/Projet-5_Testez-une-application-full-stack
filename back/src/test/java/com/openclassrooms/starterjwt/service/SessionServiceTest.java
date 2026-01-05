package com.openclassrooms.starterjwt.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.SessionService;

import java.util.ArrayList;
import java.util.Optional;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;


import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;



@ExtendWith(MockitoExtension.class)

public class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SessionService sessionService;

    @Test
    public void participate_AddUserToSession() {
        // ARRANGE
        Session session = new Session();
        session.setId(4L);
        session.setUsers(new ArrayList<>());
        
        User user = new User();
        user.setId(1L);
        
        when(sessionRepository.findById(4L)).thenReturn(Optional.of(session));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        
        // ACT
        sessionService.participate(4L, 1L);
        
        // ASSERT
        verify(sessionRepository).save(session);
        assertThat(session.getUsers()).contains(user);
    }

    @Test
    public void participate_SessionNotFound() {
        // ARRANGE
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());
        
        // ASSERT
        assertThrows(NotFoundException.class, () -> {
            sessionService.participate(1L, 1L);
        });
    }

    @Test
    public void participate_UserAlreadyParticipating() {
        // ARRANGE
        Session session = new Session();
        session.setId(1L);
        session.setUsers(new ArrayList<>());

        User user = new User();
        user.setId(1L);
        
        session.getUsers().add(user);

        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        
        // ASSERT
        assertThrows(BadRequestException.class, () -> {
            sessionService.participate(1L, 1L);
        });
    }

    @Test
    public void noLongerParticipate_DeleteUserFromSession() {
        // ARRANGE
        Session session = new Session();
        session.setId(1L);
        session.setUsers(new ArrayList<>());

        User user = new User();
        user.setId(6L);
        
        session.getUsers().add(user);

        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        
        // ACT
        sessionService.noLongerParticipate(1L, 6L);
        
        // ASSERT
        assertThat(session.getUsers()).doesNotContain(user);
        assertThat(session.getUsers()).isEmpty();
        verify(sessionRepository).save(session);
    }

    @Test
    public void noLongerParticipate_SessionNotFound() {
        // ARRANGE
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());
        
        // ASSERT
        assertThrows(NotFoundException.class, () -> {
            sessionService.noLongerParticipate(1L, 1L);
        });
    }

    @Test
    public void noLongerParticipate_UserNotParticipating() {
        // ARRANGE
        Session session = new Session();
        session.setId(1L);
        session.setUsers(new ArrayList<>());

        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        
        // ASSERT
        assertThrows(BadRequestException.class, () -> {
            sessionService.noLongerParticipate(1L, 1L);
        });
    }

}
