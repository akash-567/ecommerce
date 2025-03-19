package com.example.ecommerce.security;

import com.example.ecommerce.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtTokenProviderTest {

    @InjectMocks
    private JwtTokenProvider jwtTokenProvider;

    private Authentication authentication;
    private User user;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtSecret", "testSecretKey123456789012345678901234567890");
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtExpirationInMs", 3600000);
        ReflectionTestUtils.invokeMethod(jwtTokenProvider, "init");

        user = new User();
        user.setUsername("testuser");
    }

    @Test
    void generateToken_Success() {
        authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);

        String token = jwtTokenProvider.generateToken(authentication);

        assertNotNull(token);
        assertTrue(jwtTokenProvider.validateToken(token));
        assertEquals("testuser", jwtTokenProvider.getUsernameFromJWT(token));
    }

    @Test
    void validateToken_ValidToken() {
        authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);

        String token = jwtTokenProvider.generateToken(authentication);
        assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    void validateToken_InvalidToken() {
        assertFalse(jwtTokenProvider.validateToken("invalid-token"));
    }

    @Test
    void getUsernameFromJWT_Success() {
        authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);

        String token = jwtTokenProvider.generateToken(authentication);
        assertEquals("testuser", jwtTokenProvider.getUsernameFromJWT(token));
    }
} 