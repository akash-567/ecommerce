package com.example.ecommerce.service;

import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.OrderStatusMessage;
import org.springframework.stereotype.Service;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@Service
public class OrderMessageProducer {

    private final RabbitTemplate rabbitTemplate;

    public OrderMessageProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendOrderCreatedMessage(Order order) {
        // In a real implementation, this would send a message to a message broker
        // like Kafka, RabbitMQ, etc.
        System.out.println("Order created message sent for order: " + order.getOrderNumber());
    }
    
    public void sendOrderStatusChangedMessage(Order order) {
        // In a real implementation, this would send a message to a message broker
        System.out.println("Order status changed to " + order.getStatus() + 
                " for order: " + order.getOrderNumber());
    }

    public void sendOrderStatusUpdate(OrderStatusMessage statusMessage) {
        String routingKey = "order.status." + 
            (statusMessage.getStatus() != null ? statusMessage.getStatus().toLowerCase() : "unknown");
        
        rabbitTemplate.convertAndSend("order-status-exchange", routingKey, statusMessage);
        System.out.println("Order status update sent for order: " + statusMessage.getOrderId() + 
                " with status: " + statusMessage.getStatus());
    }
} 