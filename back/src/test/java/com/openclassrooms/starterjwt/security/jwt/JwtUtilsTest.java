package com.openclassrooms.starterjwt.security.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(MockitoExtension.class)
public class JwtUtilsTest {

    private JwtUtils jwtUtils;

    @Mock
    private Authentication authentication;

    @BeforeEach
    public void setUp() {
        jwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "macletokenjwt");
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 1000000);
    }

    @Test
    public void validateJwtToken_Success() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
            .id(1L)
            .username("user@user.com")
            .lastName("user")
            .firstName("user")
            .password("password")
            .admin(true)
            .build();

        when(authentication.getPrincipal()).thenReturn(userDetails);
        String token = jwtUtils.generateJwtToken(authentication);

        boolean result = jwtUtils.validateJwtToken(token);

        assertEquals(true, result);
        assertEquals(true, userDetails.getAdmin());
    }

    @Test
    public void validateJwtToken_InvalidSignature() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
            .id(1L)
            .username("invalid@invalid.com")
            .lastName("invalid")
            .firstName("invalid")
            .password("password")
            .admin(false)
            .build();

        when(authentication.getPrincipal()).thenReturn(userDetails);
        String token = jwtUtils.generateJwtToken(authentication);

        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "nouvelleclesecret");

        boolean result = jwtUtils.validateJwtToken(token);

        assertEquals(false, result);
        assertEquals(false, userDetails.getAdmin());
    }

    @Test
    public void valideJwtToken_MalformedJwt() {
        String token = "invalidToken";
        boolean result = jwtUtils.validateJwtToken(token);
        assertEquals(false, result);
    }

    @Test
    public void valideJwtToken_ExpiredJwt() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
            .id(1L)
            .username("expired@expired.com")
            .lastName("expired")
            .firstName("expired")
            .password("password")
            .build();

        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 0);

        when(authentication.getPrincipal()).thenReturn(userDetails);
        String token = jwtUtils.generateJwtToken(authentication);

        boolean result = jwtUtils.validateJwtToken(token);
        assertEquals(false, result);
    }

    @Test
    public void valideJwtToken_IllegalArgument() {
        String token = null;
        boolean result = jwtUtils.validateJwtToken(token);
        assertEquals(false, result);
    }

}
