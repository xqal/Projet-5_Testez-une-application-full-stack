package com.openclassrooms.starterjwt.security.services;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserDetailsImplTest {

    @Test
    public void testUserDetailsProperties() {
        // ARRANGE
        UserDetailsImpl user = UserDetailsImpl.builder()
                .id(1L)
                .build();
        
        UserDetailsImpl user2 = UserDetailsImpl.builder()
                .id(1L)
                .build();
        
        UserDetailsImpl user3 = UserDetailsImpl.builder()
                .id(3L)
                .build();

        // ACT & ASSERT
        assertThat(user.equals(user)).isTrue();

        assertThat(user.equals(null)).isFalse();
        assertThat(user.equals(new Object())).isFalse();

        assertThat(user.equals(user2)).isTrue(); 
        assertThat(user.equals(user3)).isFalse();
    }
}
