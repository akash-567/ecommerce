package com.example.ecommerce.service;

import com.example.ecommerce.model.OrderStatusMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderMessageProducerTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private OrderMessageProducer orderMessageProducer;

    private OrderStatusMessage testMessage;

    @BeforeEach
    void setUp() {
        testMessage = new OrderStatusMessage();
        testMessage.setOrderId(1L);
        testMessage.setStatus("PROCESSING");
        testMessage.setMessage("Order is being processed");
    }

    @Test
    void sendOrderStatusUpdate_Success() {
        orderMessageProducer.sendOrderStatusUpdate(testMessage);

        verify(rabbitTemplate).convertAndSend(
            eq("order-status-exchange"),
            eq("order.status." + testMessage.getStatus().toLowerCase()),
            any(OrderStatusMessage.class)
        );
    }

    @Test
    void sendOrderStatusUpdate_WithNullStatus() {
        testMessage.setStatus(null);
        orderMessageProducer.sendOrderStatusUpdate(testMessage);

        verify(rabbitTemplate).convertAndSend(
            eq("order-status-exchange"),
            eq("order.status.unknown"),
            any(OrderStatusMessage.class)
        );
    }
} 