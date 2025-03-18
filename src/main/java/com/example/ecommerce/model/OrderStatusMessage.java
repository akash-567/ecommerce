package com.example.ecommerce.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class OrderStatusMessage {
    private Long orderId;
    private String status;
    private String message;
    private LocalDateTime timestamp;
    private String customerEmail;
} 