package com.example.ecommerce.service;

import com.example.ecommerce.config.RabbitMQConfig;
import com.example.ecommerce.model.OrderStatusMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderMessageProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendOrderStatusUpdate(OrderStatusMessage message) {
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.ORDER_STATUS_EXCHANGE,
            "order.status." + message.getStatus().toLowerCase(),
            message
        );
    }
} 