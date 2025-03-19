package com.example.ecommerce.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void testProduct() {
        Product product = new Product();
        
        product.setId(1L);
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(new BigDecimal("100.00"));
        product.setStockQuantity(10);

        assertEquals(1L, product.getId());
        assertEquals("Test Product", product.getName());
        assertEquals("Test Description", product.getDescription());
        assertEquals(new BigDecimal("100.00"), product.getPrice());
        assertEquals(10, product.getStockQuantity());
    }

    @Test
    void testProductOrderItems() {
        Product product = new Product();
        OrderItem orderItem = new OrderItem();
        orderItem.setId(1L);
        orderItem.setQuantity(2);
        orderItem.setPrice(new BigDecimal("50.00"));
        product.getOrderItems().add(orderItem);

        assertEquals(1, product.getOrderItems().size());
        assertTrue(product.getOrderItems().contains(orderItem));
    }

    @Test
    void testProductEquals() {
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Test Product");
        product1.setDescription("Test Description");
        product1.setPrice(new BigDecimal("100.00"));
        product1.setStockQuantity(10);

        Product product2 = new Product();
        product2.setId(1L);
        product2.setName("Test Product");
        product2.setDescription("Test Description");
        product2.setPrice(new BigDecimal("100.00"));
        product2.setStockQuantity(10);

        assertEquals(product1, product2);
    }

    @Test
    void testProductHashCode() {
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Test Product");
        product1.setDescription("Test Description");
        product1.setPrice(new BigDecimal("100.00"));
        product1.setStockQuantity(10);

        Product product2 = new Product();
        product2.setId(1L);
        product2.setName("Test Product");
        product2.setDescription("Test Description");
        product2.setPrice(new BigDecimal("100.00"));
        product2.setStockQuantity(10);

        assertEquals(product1.hashCode(), product2.hashCode());
    }
} 