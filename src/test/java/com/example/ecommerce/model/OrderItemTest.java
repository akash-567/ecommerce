package com.example.ecommerce.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class OrderItemTest {

    @Test
    void testOrderItem() {
        OrderItem orderItem = new OrderItem();
        
        orderItem.setId(1L);
        orderItem.setQuantity(2);
        orderItem.setPrice(new BigDecimal("50.00"));
        orderItem.setSubtotal(new BigDecimal("100.00"));

        assertEquals(1L, orderItem.getId());
        assertEquals(2, orderItem.getQuantity());
        assertEquals(new BigDecimal("50.00"), orderItem.getPrice());
        assertEquals(new BigDecimal("100.00"), orderItem.getSubtotal());
    }

    @Test
    void testOrderItemOrder() {
        OrderItem orderItem = new OrderItem();
        Order order = new Order();
        order.setId(1L);
        order.setOrderNumber("ORD-001");
        orderItem.setOrder(order);

        assertEquals(order, orderItem.getOrder());
        assertEquals(1L, orderItem.getOrder().getId());
        assertEquals("ORD-001", orderItem.getOrder().getOrderNumber());
    }

    @Test
    void testOrderItemProduct() {
        OrderItem orderItem = new OrderItem();
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        orderItem.setProduct(product);

        assertEquals(product, orderItem.getProduct());
        assertEquals(1L, orderItem.getProduct().getId());
        assertEquals("Test Product", orderItem.getProduct().getName());
    }

    @Test
    void testOrderItemEquals() {
        OrderItem orderItem1 = new OrderItem();
        orderItem1.setId(1L);
        orderItem1.setQuantity(2);
        orderItem1.setPrice(new BigDecimal("50.00"));

        OrderItem orderItem2 = new OrderItem();
        orderItem2.setId(1L);
        orderItem2.setQuantity(2);
        orderItem2.setPrice(new BigDecimal("50.00"));

        assertEquals(orderItem1, orderItem2);
    }

    @Test
    void testOrderItemHashCode() {
        OrderItem orderItem1 = new OrderItem();
        orderItem1.setId(1L);
        orderItem1.setQuantity(2);
        orderItem1.setPrice(new BigDecimal("50.00"));

        OrderItem orderItem2 = new OrderItem();
        orderItem2.setId(1L);
        orderItem2.setQuantity(2);
        orderItem2.setPrice(new BigDecimal("50.00"));

        assertEquals(orderItem1.hashCode(), orderItem2.hashCode());
    }
} 