package com.example.ecommerce.controller;

import com.example.ecommerce.dto.AuthRequest;
import com.example.ecommerce.dto.AuthResponse;
import com.example.ecommerce.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthService authService;

    @Test
    void testRegister() {
        // Arrange
        AuthRequest request = new AuthRequest();
        request.setUsername("testuser");
        request.setPassword("password");
        request.setEmail("test@example.com");

        AuthResponse response = new AuthResponse();
        response.setToken("test-token");

        when(authService.register(request)).thenReturn(response);

        // Act
        ResponseEntity<AuthResponse> result = authController.register(request);

        // Assert
        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(response, result.getBody());
        verify(authService).register(request);
    }

    @Test
    void testLogin() {
        // Arrange
        AuthRequest request = new AuthRequest();
        request.setUsername("testuser");
        request.setPassword("password");

        AuthResponse response = new AuthResponse();
        response.setToken("test-token");

        when(authService.login(request)).thenReturn(response);

        // Act
        ResponseEntity<AuthResponse> result = authController.login(request);

        // Assert
        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(response, result.getBody());
        verify(authService).login(request);
    }
} 