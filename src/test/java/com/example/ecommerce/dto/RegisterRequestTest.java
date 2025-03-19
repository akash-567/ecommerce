package com.example.ecommerce.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegisterRequestTest {

    @Test
    void testRegisterRequest() {
        RegisterRequest registerRequest = new RegisterRequest();
        
        registerRequest.setUsername("testuser");
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password");

        assertEquals("testuser", registerRequest.getUsername());
        assertEquals("test@example.com", registerRequest.getEmail());
        assertEquals("password", registerRequest.getPassword());
    }

    @Test
    void testRegisterRequestBuilder() {
        RegisterRequest registerRequest = RegisterRequest.builder()
            .username("testuser")
            .email("test@example.com")
            .password("password")
            .build();

        assertEquals("testuser", registerRequest.getUsername());
        assertEquals("test@example.com", registerRequest.getEmail());
        assertEquals("password", registerRequest.getPassword());
    }

    @Test
    void testRegisterRequestEquals() {
        RegisterRequest registerRequest1 = new RegisterRequest();
        registerRequest1.setUsername("testuser");
        registerRequest1.setEmail("test@example.com");
        registerRequest1.setPassword("password");

        RegisterRequest registerRequest2 = new RegisterRequest();
        registerRequest2.setUsername("testuser");
        registerRequest2.setEmail("test@example.com");
        registerRequest2.setPassword("password");

        assertEquals(registerRequest1, registerRequest2);
    }

    @Test
    void testRegisterRequestHashCode() {
        RegisterRequest registerRequest1 = new RegisterRequest();
        registerRequest1.setUsername("testuser");
        registerRequest1.setEmail("test@example.com");
        registerRequest1.setPassword("password");

        RegisterRequest registerRequest2 = new RegisterRequest();
        registerRequest2.setUsername("testuser");
        registerRequest2.setEmail("test@example.com");
        registerRequest2.setPassword("password");

        assertEquals(registerRequest1.hashCode(), registerRequest2.hashCode());
    }
} 