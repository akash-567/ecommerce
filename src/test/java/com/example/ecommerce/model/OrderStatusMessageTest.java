package com.example.ecommerce.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class OrderStatusMessageTest {

    @Test
    void testOrderStatusMessage() {
        OrderStatusMessage statusMessage = new OrderStatusMessage();
        
        statusMessage.setOrderId(1L);
        statusMessage.setStatus(Order.OrderStatus.PENDING.name());
        statusMessage.setMessage("Order is pending");
        statusMessage.setTimestamp(LocalDateTime.now());
        statusMessage.setCustomerEmail("test@example.com");

        assertEquals(1L, statusMessage.getOrderId());
        assertEquals("PENDING", statusMessage.getStatus());
        assertEquals("Order is pending", statusMessage.getMessage());
        assertNotNull(statusMessage.getTimestamp());
        assertEquals("test@example.com", statusMessage.getCustomerEmail());
    }

    @Test
    void testOrderStatusMessageEquals() {
        OrderStatusMessage statusMessage1 = new OrderStatusMessage();
        statusMessage1.setOrderId(1L);
        statusMessage1.setStatus(Order.OrderStatus.CONFIRMED.name());
        statusMessage1.setMessage("Order is confirmed");
        statusMessage1.setTimestamp(LocalDateTime.now());
        statusMessage1.setCustomerEmail("test@example.com");

        OrderStatusMessage statusMessage2 = new OrderStatusMessage();
        statusMessage2.setOrderId(1L);
        statusMessage2.setStatus(Order.OrderStatus.CONFIRMED.name());
        statusMessage2.setMessage("Order is confirmed");
        statusMessage2.setTimestamp(statusMessage1.getTimestamp());
        statusMessage2.setCustomerEmail("test@example.com");

        assertEquals(statusMessage1, statusMessage2);
    }

    @Test
    void testOrderStatusMessageHashCode() {
        OrderStatusMessage statusMessage1 = new OrderStatusMessage();
        statusMessage1.setOrderId(1L);
        statusMessage1.setStatus(Order.OrderStatus.CONFIRMED.name());
        statusMessage1.setMessage("Order is confirmed");
        statusMessage1.setTimestamp(LocalDateTime.now());
        statusMessage1.setCustomerEmail("test@example.com");

        OrderStatusMessage statusMessage2 = new OrderStatusMessage();
        statusMessage2.setOrderId(1L);
        statusMessage2.setStatus(Order.OrderStatus.CONFIRMED.name());
        statusMessage2.setMessage("Order is confirmed");
        statusMessage2.setTimestamp(statusMessage1.getTimestamp());
        statusMessage2.setCustomerEmail("test@example.com");

        assertEquals(statusMessage1.hashCode(), statusMessage2.hashCode());
    }
} 