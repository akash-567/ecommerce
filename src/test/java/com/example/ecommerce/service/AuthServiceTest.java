package com.example.ecommerce.service;

import com.example.ecommerce.dto.AuthRequest;
import com.example.ecommerce.dto.AuthResponse;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider tokenProvider;

    @Mock
    private AuthenticationManager authenticationManager;

    private AuthRequest loginRequest;
    private AuthRequest registerRequest;
    private User user;
    private Authentication authentication;
    private String token;

    @BeforeEach
    void setUp() {
        loginRequest = new AuthRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password");

        registerRequest = new AuthRequest();
        registerRequest.setUsername("testuser");
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password");

        user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");

        authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        token = "test-token";
    }

    @Test
    void login_Success() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(authentication);
        when(tokenProvider.generateToken(authentication)).thenReturn(token);

        AuthResponse response = authService.login(loginRequest);

        assertNotNull(response);
        assertEquals(token, response.getToken());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenProvider).generateToken(authentication);
    }

    @Test
    void register_Success() {
        when(userRepository.findByUsername(registerRequest.getUsername())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(authentication);
        when(tokenProvider.generateToken(authentication)).thenReturn(token);

        AuthResponse response = authService.register(registerRequest);

        assertNotNull(response);
        assertEquals(token, response.getToken());
        verify(userRepository).findByUsername(registerRequest.getUsername());
        verify(userRepository).findByEmail(registerRequest.getEmail());
        verify(passwordEncoder).encode(registerRequest.getPassword());
        verify(userRepository).save(any(User.class));
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenProvider).generateToken(authentication);
    }

    @Test
    void register_UsernameExists() {
        when(userRepository.findByUsername(registerRequest.getUsername()))
            .thenReturn(Optional.of(user));

        assertThrows(IllegalStateException.class, () -> authService.register(registerRequest));
        verify(userRepository).findByUsername(registerRequest.getUsername());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void register_EmailExists() {
        when(userRepository.findByUsername(registerRequest.getUsername())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(registerRequest.getEmail()))
            .thenReturn(Optional.of(user));

        assertThrows(IllegalStateException.class, () -> authService.register(registerRequest));
        verify(userRepository).findByUsername(registerRequest.getUsername());
        verify(userRepository).findByEmail(registerRequest.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }
} 