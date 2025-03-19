package com.example.ecommerce.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    private Order order;
    private User user;
    private OrderItem orderItem;
    private Product product;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setPrice(new BigDecimal("100.00"));

        orderItem = new OrderItem();
        orderItem.setId(1L);
        orderItem.setProduct(product);
        orderItem.setQuantity(2);
        orderItem.setPrice(new BigDecimal("100.00"));

        order = new Order();
        order.setId(1L);
        order.setOrderNumber("ORD-001");
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Order.OrderStatus.PENDING);
        order.setOrderItems(new ArrayList<>());
        order.getOrderItems().add(orderItem);
        orderItem.setOrder(order);
        order.setTotalAmount(new BigDecimal("200.00"));
    }

    @Test
    void testOrderCreation() {
        assertNotNull(order);
        assertEquals(1L, order.getId());
        assertEquals("ORD-001", order.getOrderNumber());
        assertEquals(user, order.getUser());
        assertNotNull(order.getOrderDate());
        assertEquals(Order.OrderStatus.PENDING, order.getStatus());
        assertEquals(1, order.getOrderItems().size());
        assertEquals(new BigDecimal("200.00"), order.getTotalAmount());
    }

    @Test
    void testSettersAndGetters() {
        Order newOrder = new Order();
        
        newOrder.setId(2L);
        assertEquals(2L, newOrder.getId());

        newOrder.setOrderNumber("ORD-002");
        assertEquals("ORD-002", newOrder.getOrderNumber());

        newOrder.setUser(user);
        assertEquals(user, newOrder.getUser());

        LocalDateTime now = LocalDateTime.now();
        newOrder.setOrderDate(now);
        assertEquals(now, newOrder.getOrderDate());

        newOrder.setStatus(Order.OrderStatus.CONFIRMED);
        assertEquals(Order.OrderStatus.CONFIRMED, newOrder.getStatus());

        List<OrderItem> items = new ArrayList<>();
        items.add(orderItem);
        newOrder.setOrderItems(items);
        assertEquals(1, newOrder.getOrderItems().size());
        assertTrue(newOrder.getOrderItems().contains(orderItem));

        newOrder.setTotalAmount(new BigDecimal("300.00"));
        assertEquals(new BigDecimal("300.00"), newOrder.getTotalAmount());
    }

    @Test
    void testStatusTransitions() {
        order.setStatus(Order.OrderStatus.CONFIRMED);
        assertEquals(Order.OrderStatus.CONFIRMED, order.getStatus());

        order.setStatus(Order.OrderStatus.SHIPPED);
        assertEquals(Order.OrderStatus.SHIPPED, order.getStatus());

        order.setStatus(Order.OrderStatus.DELIVERED);
        assertEquals(Order.OrderStatus.DELIVERED, order.getStatus());

        order.setStatus(Order.OrderStatus.CANCELLED);
        assertEquals(Order.OrderStatus.CANCELLED, order.getStatus());
    }

    @Test
    void testOrderItems() {
        OrderItem newOrderItem = new OrderItem();
        newOrderItem.setId(2L);
        newOrderItem.setProduct(product);
        newOrderItem.setQuantity(1);
        newOrderItem.setPrice(new BigDecimal("100.00"));
        order.getOrderItems().add(newOrderItem);

        assertEquals(2, order.getOrderItems().size());
        assertTrue(order.getOrderItems().contains(newOrderItem));
    }

    @Test
    void testTotalAmountCalculation() {
        assertEquals(new BigDecimal("200.00"), order.getTotalAmount());
    }

    @Test
    void testOrderNumberFormat() {
        assertTrue(order.getOrderNumber().startsWith("ORD-"));
        assertTrue(order.getOrderNumber().length() > 4);
    }
} 