package com.example.ecommerce.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginRequestTest {

    @Test
    void testLoginRequest() {
        LoginRequest loginRequest = new LoginRequest();
        
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password");

        assertEquals("testuser", loginRequest.getUsername());
        assertEquals("password", loginRequest.getPassword());
    }

    @Test
    void testLoginRequestBuilder() {
        LoginRequest loginRequest = LoginRequest.builder()
            .username("testuser")
            .password("password")
            .build();

        assertEquals("testuser", loginRequest.getUsername());
        assertEquals("password", loginRequest.getPassword());
    }

    @Test
    void testLoginRequestEquals() {
        LoginRequest loginRequest1 = new LoginRequest();
        loginRequest1.setUsername("testuser");
        loginRequest1.setPassword("password");

        LoginRequest loginRequest2 = new LoginRequest();
        loginRequest2.setUsername("testuser");
        loginRequest2.setPassword("password");

        assertEquals(loginRequest1, loginRequest2);
    }

    @Test
    void testLoginRequestHashCode() {
        LoginRequest loginRequest1 = new LoginRequest();
        loginRequest1.setUsername("testuser");
        loginRequest1.setPassword("password");

        LoginRequest loginRequest2 = new LoginRequest();
        loginRequest2.setUsername("testuser");
        loginRequest2.setPassword("password");

        assertEquals(loginRequest1.hashCode(), loginRequest2.hashCode());
    }
} 