package com.example.ecommerce.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserDTOTest {

    @Test
    void testUserDTO() {
        UserDTO userDTO = new UserDTO();
        
        userDTO.setId(1L);
        userDTO.setUsername("testuser");
        userDTO.setEmail("test@example.com");
        userDTO.setPassword("password");

        assertEquals(1L, userDTO.getId());
        assertEquals("testuser", userDTO.getUsername());
        assertEquals("test@example.com", userDTO.getEmail());
        assertEquals("password", userDTO.getPassword());
    }

    @Test
    void testUserDTOBuilder() {
        UserDTO userDTO = UserDTO.builder()
            .id(1L)
            .username("testuser")
            .email("test@example.com")
            .password("password")
            .build();

        assertEquals(1L, userDTO.getId());
        assertEquals("testuser", userDTO.getUsername());
        assertEquals("test@example.com", userDTO.getEmail());
        assertEquals("password", userDTO.getPassword());
    }

    @Test
    void testUserDTOEquals() {
        UserDTO userDTO1 = new UserDTO();
        userDTO1.setId(1L);
        userDTO1.setUsername("testuser");
        userDTO1.setEmail("test@example.com");
        userDTO1.setPassword("password");

        UserDTO userDTO2 = new UserDTO();
        userDTO2.setId(1L);
        userDTO2.setUsername("testuser");
        userDTO2.setEmail("test@example.com");
        userDTO2.setPassword("password");

        assertEquals(userDTO1, userDTO2);
    }

    @Test
    void testUserDTOHashCode() {
        UserDTO userDTO1 = new UserDTO();
        userDTO1.setId(1L);
        userDTO1.setUsername("testuser");
        userDTO1.setEmail("test@example.com");
        userDTO1.setPassword("password");

        UserDTO userDTO2 = new UserDTO();
        userDTO2.setId(1L);
        userDTO2.setUsername("testuser");
        userDTO2.setEmail("test@example.com");
        userDTO2.setPassword("password");

        assertEquals(userDTO1.hashCode(), userDTO2.hashCode());
    }
} 