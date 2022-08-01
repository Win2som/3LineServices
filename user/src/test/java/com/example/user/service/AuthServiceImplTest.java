package com.example.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.user.config.JwtUtil;
import com.example.user.dto.AuthRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {AuthServiceImpl.class})
@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {
    @InjectMocks
    private AuthServiceImpl authServiceImpl;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    /**
     * Method under test: {@link AuthServiceImpl#login(AuthRequest)}
     */
    @Test
    void givenGoodAuthRequest_whenLogin_thenAuthenticate() throws AuthenticationException {
        when(jwtUtil.generateToken((Authentication) any())).thenReturn("ABC123");
        when(authenticationManager.authenticate((Authentication) any()))
                .thenReturn(new TestingAuthenticationToken("Principal", "Credentials"));
        assertEquals("Bearer ABC123", authServiceImpl.login(new AuthRequest("winijay2@gmail.com", "1234567")));
        verify(jwtUtil).generateToken((Authentication) any());
        verify(authenticationManager).authenticate((Authentication) any());
    }

    /**
     * Method under test: {@link AuthServiceImpl#login(AuthRequest)}
     */
    @Test
    void givenAuthenticationError_whenLogin_throwAuthenticationException () throws AuthenticationException {
        when(authenticationManager.authenticate((Authentication) any()))
                .thenThrow(new RuntimeException("An error occurred"));
        assertThrows(RuntimeException.class,
                () -> authServiceImpl.login(new AuthRequest("winijay2@gmail.com", "1234567")));
        verify(authenticationManager).authenticate((Authentication) any());
    }


    /**
     * Method under test: {@link AuthServiceImpl#login(AuthRequest)}
     */
    @Test
    void givenNullAuthRequest_whenLogin_throwAuthenticationException() throws AuthenticationException {
        assertThrows(RuntimeException.class, () -> authServiceImpl.login(null));
    }

}

