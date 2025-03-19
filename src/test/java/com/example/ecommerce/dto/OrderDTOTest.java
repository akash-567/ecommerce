package com.example.ecommerce.dto;

import com.example.ecommerce.model.Order;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderDTOTest {

    @Test
    void testOrderDTO() {
        OrderDTO orderDTO = new OrderDTO();
        
        orderDTO.setId(1L);
        orderDTO.setOrderNumber("ORD-001");
        orderDTO.setTotalAmount(new BigDecimal("100.00"));
        orderDTO.setStatus(Order.OrderStatus.PENDING);
        orderDTO.setCreatedAt(LocalDateTime.now());
        orderDTO.setUpdatedAt(LocalDateTime.now());

        assertEquals(1L, orderDTO.getId());
        assertEquals("ORD-001", orderDTO.getOrderNumber());
        assertEquals(new BigDecimal("100.00"), orderDTO.getTotalAmount());
        assertEquals(Order.OrderStatus.PENDING, orderDTO.getStatus());
        assertNotNull(orderDTO.getCreatedAt());
        assertNotNull(orderDTO.getUpdatedAt());
    }

    @Test
    void testOrderDTOItems() {
        OrderDTO orderDTO = new OrderDTO();
        List<OrderItemDTO> items = new ArrayList<>();
        OrderItemDTO item = new OrderItemDTO();
        item.setId(1L);
        item.setQuantity(2);
        item.setPrice(new BigDecimal("50.00"));
        items.add(item);
        orderDTO.setOrderItems(items);

        assertEquals(1, orderDTO.getOrderItems().size());
        assertTrue(orderDTO.getOrderItems().contains(item));
    }

    @Test
    void testOrderDTOUser() {
        OrderDTO orderDTO = new OrderDTO();
        UserDTO user = new UserDTO();
        user.setId(1L);
        user.setUsername("testuser");
        orderDTO.setUser(user);

        assertEquals(user, orderDTO.getUser());
        assertEquals(1L, orderDTO.getUser().getId());
        assertEquals("testuser", orderDTO.getUser().getUsername());
    }

    @Test
    void testOrderDTOEquals() {
        OrderDTO orderDTO1 = new OrderDTO();
        orderDTO1.setId(1L);
        orderDTO1.setOrderNumber("ORD-001");
        orderDTO1.setTotalAmount(new BigDecimal("100.00"));
        orderDTO1.setStatus(Order.OrderStatus.PENDING);

        OrderDTO orderDTO2 = new OrderDTO();
        orderDTO2.setId(1L);
        orderDTO2.setOrderNumber("ORD-001");
        orderDTO2.setTotalAmount(new BigDecimal("100.00"));
        orderDTO2.setStatus(Order.OrderStatus.PENDING);

        assertEquals(orderDTO1, orderDTO2);
    }

    @Test
    void testOrderDTOHashCode() {
        OrderDTO orderDTO1 = new OrderDTO();
        orderDTO1.setId(1L);
        orderDTO1.setOrderNumber("ORD-001");
        orderDTO1.setTotalAmount(new BigDecimal("100.00"));
        orderDTO1.setStatus(Order.OrderStatus.PENDING);

        OrderDTO orderDTO2 = new OrderDTO();
        orderDTO2.setId(1L);
        orderDTO2.setOrderNumber("ORD-001");
        orderDTO2.setTotalAmount(new BigDecimal("100.00"));
        orderDTO2.setStatus(Order.OrderStatus.PENDING);

        assertEquals(orderDTO1.hashCode(), orderDTO2.hashCode());
    }
} 