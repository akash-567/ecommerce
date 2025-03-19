package com.example.ecommerce.service;

import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class CommunicationServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private CommunicationService communicationService;

    private User testUser;
    private Order testOrder;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");

        testOrder = new Order();
        testOrder.setId(1L);
        testOrder.setOrderNumber("ORD-001");
        testOrder.setUser(testUser);
    }

    @Test
    void sendOrderConfirmation_Success() {
        communicationService.sendOrderConfirmation(testOrder);

        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendOrderConfirmation_NoEmail() {
        testUser.setEmail(null);
        testOrder.setUser(testUser);

        communicationService.sendOrderConfirmation(testOrder);

        verify(mailSender, never()).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendOrderStatusUpdate_Success() {
        communicationService.sendOrderStatusUpdate(testOrder, "PROCESSING");

        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendOrderStatusUpdate_NoEmail() {
        testUser.setEmail(null);
        testOrder.setUser(testUser);

        communicationService.sendOrderStatusUpdate(testOrder, "PROCESSING");

        verify(mailSender, never()).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendWelcomeEmail_Success() {
        communicationService.sendWelcomeEmail(testUser);

        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendWelcomeEmail_NoEmail() {
        testUser.setEmail(null);

        communicationService.sendWelcomeEmail(testUser);

        verify(mailSender, never()).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendPasswordResetEmail_Success() {
        String resetToken = "test-reset-token";
        communicationService.sendPasswordResetEmail(testUser, resetToken);

        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendPasswordResetEmail_NoEmail() {
        testUser.setEmail(null);
        String resetToken = "test-reset-token";

        communicationService.sendPasswordResetEmail(testUser, resetToken);

        verify(mailSender, never()).send(any(SimpleMailMessage.class));
    }
} 