package com.example.ecommerce.dto;

import com.example.ecommerce.model.Order;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrderDTO {
    private Long id;
    
    @NotNull(message = "Order number is required")
    private String orderNumber;
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    private UserDTO user;
    
    @NotNull(message = "Order date is required")
    private LocalDateTime orderDate;

    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    @NotNull(message = "Order status is required")
    private Order.OrderStatus status;
    
    @NotEmpty(message = "Order must contain at least one item")
    @Valid
    private List<OrderItemDTO> orderItems = new ArrayList<>();
    
    @NotNull(message = "Total amount is required")
    @Min(value = 0, message = "Total amount must be greater than or equal to zero")
    private BigDecimal totalAmount;
} 