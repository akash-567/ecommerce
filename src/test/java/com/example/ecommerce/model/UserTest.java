package com.example.ecommerce.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setOrders(new ArrayList<>());
    }

    @Test
    void testUserCreation() {
        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("testuser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
        assertNotNull(user.getOrders());
        assertTrue(user.getOrders().isEmpty());
    }

    @Test
    void testUserOrders() {
        Order order = new Order();
        order.setId(1L);
        order.setUser(user);
        order.setStatus(Order.OrderStatus.PENDING);

        List<Order> orders = new ArrayList<>();
        orders.add(order);
        user.setOrders(orders);

        assertFalse(user.getOrders().isEmpty());
        assertEquals(1, user.getOrders().size());
        assertEquals(order, user.getOrders().get(0));
    }

    @Test
    void testUserEquals() {
        User user2 = new User();
        user2.setId(1L);
        user2.setUsername("testuser");
        user2.setEmail("test@example.com");
        user2.setPassword("password123");
        user2.setOrders(new ArrayList<>());

        assertEquals(user, user2);
        assertEquals(user.hashCode(), user2.hashCode());
    }

    @Test
    void testUserNotEquals() {
        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("testuser2");
        user2.setEmail("test2@example.com");
        user2.setPassword("password456");
        user2.setOrders(new ArrayList<>());

        assertNotEquals(user, user2);
        assertNotEquals(user.hashCode(), user2.hashCode());
    }
} 