package com.example.ecommerce.dto;

import com.example.ecommerce.model.Order;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {
    private Long id;
    private Long userId;
    private LocalDateTime orderDate;
    private Order.OrderStatus status;
    private List<OrderItemDTO> orderItems;
    private Double totalAmount;
} 