package com.example.ecommerce.service;

import com.example.ecommerce.model.OrderStatusMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderMessageConsumer {

    private final JavaMailSender mailSender;

    @RabbitListener(queues = "${rabbitmq.order-status-queue}")
    public void handleOrderStatusUpdate(OrderStatusMessage message) {
        log.info("Received order status update: {}", message);
        
        if (message.getCustomerEmail() == null) {
            log.warn("Cannot send email notification for order #{} - customer email is null", message.getOrderId());
            return;
        }

        // Send email notification
        SimpleMailMessage emailMessage = new SimpleMailMessage();
        emailMessage.setTo(message.getCustomerEmail());
        emailMessage.setSubject("Order Status Update - Order #" + message.getOrderId());
        emailMessage.setText(String.format(
            "Your order #%d status has been updated to: %s\nMessage: %s",
            message.getOrderId(),
            message.getStatus(),
            message.getMessage()
        ));
        
        mailSender.send(emailMessage);
        log.info("Sent email notification for order #{} to {}", message.getOrderId(), message.getCustomerEmail());
    }
} 