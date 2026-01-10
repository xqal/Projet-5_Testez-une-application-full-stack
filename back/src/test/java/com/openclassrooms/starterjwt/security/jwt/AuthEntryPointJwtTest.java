package com.openclassrooms.starterjwt.security.jwt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.core.AuthenticationException;   
import org.springframework.http.MediaType;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;




@ExtendWith(MockitoExtension.class)
public class AuthEntryPointJwtTest {
    
    @InjectMocks
    private AuthEntryPointJwt authEntryPointJwt;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private AuthenticationException authException;

    @Mock
    private ServletOutputStream outputStream;

    @Test
    public void commence_Success() throws IOException, ServletException {

        when(authException.getMessage()).thenReturn("Unauthorized error:");
        when(request.getServletPath()).thenReturn("/api/session");

        when(response.getOutputStream()).thenReturn(outputStream);

        authEntryPointJwt.commence(request, response, authException);

        verify(response, times(1)).setContentType(MediaType.APPLICATION_JSON_VALUE);
        verify(response, times(1)).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

}
