package com.openclassrooms.starterjwt.security.jwt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;

import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@ExtendWith(MockitoExtension.class)
public class AuthTokenFilterTest {

    @InjectMocks
    private AuthTokenFilter authTokenFilter;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Test
    public void doFilterInternal_Success() throws ServletException, IOException {
        String jwt = "validJwt";

        UserDetailsImpl userDetails = UserDetailsImpl.builder()
            .id(1L)
            .username("user@user.com")
            .password("password")
            .build();

        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(jwtUtils.validateJwtToken(jwt)).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken(jwt)).thenReturn(userDetails.getUsername());
        when(userDetailsService.loadUserByUsername(userDetails.getUsername())).thenReturn(userDetails);
    
        ReflectionTestUtils.invokeMethod(authTokenFilter, "doFilterInternal", request, response, filterChain);


        verify(filterChain, times(1)).doFilter(request, response);
        verify(jwtUtils, times(1)).validateJwtToken(jwt);
    }

    @Test
    public void doFilterInternal_Exception() throws ServletException,IOException {
        String jwt = null;
        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(jwtUtils.validateJwtToken(jwt)).thenReturn(false);

        ReflectionTestUtils.invokeMethod(authTokenFilter, "doFilterInternal", request, response, filterChain);
        
        verify(filterChain, times(1)).doFilter(request, response);
    }
}
