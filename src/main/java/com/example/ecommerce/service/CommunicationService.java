package com.example.ecommerce.service;

import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class CommunicationService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOrderConfirmation(Order order) {
        if (order.getUser() != null && order.getUser().getEmail() != null) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(order.getUser().getEmail());
            message.setSubject("Order Confirmation - " + order.getOrderNumber());
            message.setText(String.format(
                "Dear %s,\n\nThank you for your order. Your order number is %s.\n\n" +
                "Order Details:\n" +
                "Order Number: %s\n" +
                "Total Amount: $%.2f\n" +
                "Status: %s\n\n" +
                "We will keep you updated on your order status.\n\n" +
                "Best regards,\nE-commerce Team",
                order.getUser().getUsername(),
                order.getOrderNumber(),
                order.getOrderNumber(),
                order.getTotalAmount(),
                order.getStatus()
            ));
            mailSender.send(message);
        }
    }

    public void sendOrderStatusUpdate(Order order, String status) {
        if (order.getUser() != null && order.getUser().getEmail() != null) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(order.getUser().getEmail());
            message.setSubject("Order Status Update - " + order.getOrderNumber());
            message.setText(String.format(
                "Dear %s,\n\nYour order status has been updated.\n\n" +
                "Order Details:\n" +
                "Order Number: %s\n" +
                "New Status: %s\n\n" +
                "Thank you for shopping with us!\n\n" +
                "Best regards,\nE-commerce Team",
                order.getUser().getUsername(),
                order.getOrderNumber(),
                status
            ));
            mailSender.send(message);
        }
    }

    public void sendWelcomeEmail(User user) {
        if (user.getEmail() != null) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(user.getEmail());
            message.setSubject("Welcome to Our E-commerce Platform!");
            message.setText(String.format(
                "Dear %s,\n\nWelcome to our e-commerce platform! We're excited to have you as a member.\n\n" +
                "You can now:\n" +
                "- Browse our products\n" +
                "- Place orders\n" +
                "- Track your order status\n" +
                "- Manage your profile\n\n" +
                "If you have any questions, please don't hesitate to contact our support team.\n\n" +
                "Best regards,\nE-commerce Team",
                user.getUsername()
            ));
            mailSender.send(message);
        }
    }

    public void sendPasswordResetEmail(User user, String resetToken) {
        if (user.getEmail() != null) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(user.getEmail());
            message.setSubject("Password Reset Request");
            message.setText(String.format(
                "Dear %s,\n\nYou have requested to reset your password.\n\n" +
                "Please use the following token to reset your password: %s\n\n" +
                "If you didn't request this password reset, please ignore this email.\n\n" +
                "Best regards,\nE-commerce Team",
                user.getUsername(),
                resetToken
            ));
            mailSender.send(message);
        }
    }
} 