package com.example.ecommerce.service;

import com.example.ecommerce.model.OrderStatusMessage;
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
class OrderMessageConsumerTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private OrderMessageConsumer orderMessageConsumer;

    private OrderStatusMessage testMessage;

    @BeforeEach
    void setUp() {
        testMessage = new OrderStatusMessage();
        testMessage.setOrderId(1L);
        testMessage.setStatus("PROCESSING");
        testMessage.setMessage("Order is being processed");
        testMessage.setCustomerEmail("test@example.com");
    }

    @Test
    void handleOrderStatusUpdate_Success() {
        orderMessageConsumer.handleOrderStatusUpdate(testMessage);

        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    void handleOrderStatusUpdate_WithNullEmail() {
        testMessage.setCustomerEmail(null);
        orderMessageConsumer.handleOrderStatusUpdate(testMessage);

        verify(mailSender, never()).send(any(SimpleMailMessage.class));
    }
} 