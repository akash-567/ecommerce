package com.example.ecommerce.dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class OrderItemDTOTest {

    @Test
    void testOrderItemDTO() {
        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setId(1L);
        orderItemDTO.setProductId(2L);
        orderItemDTO.setQuantity(3);
        orderItemDTO.setPrice(new BigDecimal("29.99"));

        assertEquals(1L, orderItemDTO.getId());
        assertEquals(2L, orderItemDTO.getProductId());
        assertEquals(3, orderItemDTO.getQuantity());
        assertEquals(new BigDecimal("29.99"), orderItemDTO.getPrice());
        assertEquals(new BigDecimal("89.97"), orderItemDTO.getSubtotal());
    }

    @Test
    void testOrderItemDTOBuilder() {
        OrderItemDTO orderItemDTO = OrderItemDTO.builder()
                .id(1L)
                .productId(2L)
                .quantity(3)
                .price(new BigDecimal("29.99"))
                .build();

        assertEquals(1L, orderItemDTO.getId());
        assertEquals(2L, orderItemDTO.getProductId());
        assertEquals(3, orderItemDTO.getQuantity());
        assertEquals(new BigDecimal("29.99"), orderItemDTO.getPrice());
        assertEquals(new BigDecimal("89.97"), orderItemDTO.getSubtotal());
    }

    @Test
    void testOrderItemDTOEquals() {
        OrderItemDTO orderItemDTO1 = OrderItemDTO.builder()
                .id(1L)
                .productId(2L)
                .quantity(3)
                .price(new BigDecimal("29.99"))
                .build();

        OrderItemDTO orderItemDTO2 = OrderItemDTO.builder()
                .id(1L)
                .productId(2L)
                .quantity(3)
                .price(new BigDecimal("29.99"))
                .build();

        assertEquals(orderItemDTO1, orderItemDTO2);
        assertEquals(orderItemDTO1.hashCode(), orderItemDTO2.hashCode());
    }

    @Test
    void testOrderItemDTONotEquals() {
        OrderItemDTO orderItemDTO1 = OrderItemDTO.builder()
                .id(1L)
                .productId(2L)
                .quantity(3)
                .price(new BigDecimal("29.99"))
                .build();

        OrderItemDTO orderItemDTO2 = OrderItemDTO.builder()
                .id(2L)
                .productId(3L)
                .quantity(4)
                .price(new BigDecimal("39.99"))
                .build();

        assertNotEquals(orderItemDTO1, orderItemDTO2);
        assertNotEquals(orderItemDTO1.hashCode(), orderItemDTO2.hashCode());
    }
} 