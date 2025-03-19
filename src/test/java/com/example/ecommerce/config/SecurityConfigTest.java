package com.example.ecommerce.config;

import com.example.ecommerce.security.JwtAuthenticationFilter;
import com.example.ecommerce.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.DefaultSecurityFilterChain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityConfigTest {

    @InjectMocks
    private SecurityConfig securityConfig;

    @Mock
    private UserService userService;

    @Mock
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void testPasswordEncoderBean() {
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        assertNotNull(passwordEncoder);
        assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
    }

    @Test
    void testAuthenticationProvider() {
        AuthenticationProvider provider = securityConfig.authenticationProvider();
        assertNotNull(provider);
    }

    @Test
    void testSecurityFilterChain() throws Exception {
        HttpSecurity http = mock(HttpSecurity.class);
        DefaultSecurityFilterChain filterChain = mock(DefaultSecurityFilterChain.class);
        
        when(http.csrf(any())).thenReturn(http);
        when(http.authorizeHttpRequests(any())).thenReturn(http);
        when(http.sessionManagement(any())).thenReturn(http);
        when(http.authenticationProvider(any())).thenReturn(http);
        when(http.addFilterBefore(any(), any())).thenReturn(http);
        when(http.build()).thenReturn(filterChain);

        SecurityFilterChain result = securityConfig.securityFilterChain(http);
        assertNotNull(result);
        verify(http).csrf(any());
        verify(http).authorizeHttpRequests(any());
        verify(http).sessionManagement(any());
        verify(http).authenticationProvider(any());
        verify(http).addFilterBefore(any(), any());
        verify(http).build();
    }   
} 