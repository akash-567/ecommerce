package com.example.ecommerce.service;

import com.example.ecommerce.dto.OrderDTO;
import com.example.ecommerce.dto.OrderItemDTO;
import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.OrderItem;
import com.example.ecommerce.model.OrderStatusMessage;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderMessageProducer messageProducer;

    @Transactional
    public OrderDTO createOrder(OrderDTO orderDTO, UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (orderDTO.getOrderItems() == null || orderDTO.getOrderItems().isEmpty()) {
            throw new IllegalStateException("Order must contain at least one item");
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderNumber(generateOrderNumber());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Order.OrderStatus.PENDING);
        order.setOrderItems(orderDTO.getOrderItems().stream()
                .map(itemDTO -> createOrderItem(itemDTO))
                .collect(Collectors.toList()));
        
        calculateTotalAmount(order);
        Order savedOrder = orderRepository.save(order);

        try {
            // Send status update message
            OrderStatusMessage statusMessage = new OrderStatusMessage();
            statusMessage.setOrderId(savedOrder.getId());
            statusMessage.setStatus(savedOrder.getStatus().name());
            statusMessage.setMessage("Your order has been received and is being processed.");
            statusMessage.setTimestamp(LocalDateTime.now());
            statusMessage.setCustomerEmail(savedOrder.getUser().getEmail());
            
            messageProducer.sendOrderStatusUpdate(statusMessage);
        } catch (Exception e) {
            // Log the error but don't fail the order creation
            // TODO: Implement proper logging
        }

        return convertToDTO(savedOrder);
    }

    public List<OrderDTO> getUserOrders(UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return orderRepository.findByUser(user).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public OrderDTO getOrder(Long id, UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new SecurityException("Not authorized to access this order");
        }

        return convertToDTO(order);
    }

    private OrderItem createOrderItem(OrderItemDTO itemDTO) {
        if (itemDTO.getQuantity() <= 0) {
            throw new IllegalStateException("Quantity must be greater than zero");
        }

        Product product = productRepository.findById(itemDTO.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        if (product.getStockQuantity() < itemDTO.getQuantity()) {
            throw new IllegalStateException("Insufficient stock for product: " + product.getName());
        }

        if (product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("Product price must be greater than zero");
        }

        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(itemDTO.getQuantity());
        orderItem.setPrice(product.getPrice());

        // Update product stock
        product.setStockQuantity(product.getStockQuantity() - itemDTO.getQuantity());
        productRepository.save(product);

        return orderItem;
    }

    private void calculateTotalAmount(Order order) {
        BigDecimal total = order.getOrderItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalAmount(total);
    }

    private String generateOrderNumber() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private OrderDTO convertToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setOrderNumber(order.getOrderNumber());
        dto.setUserId(order.getUser().getId());
        dto.setOrderDate(order.getOrderDate());
        dto.setStatus(order.getStatus());
        dto.setTotalAmount(order.getTotalAmount());
        
        List<OrderItemDTO> itemDTOs = order.getOrderItems().stream()
                .map(this::convertToItemDTO)
                .collect(Collectors.toList());
        dto.setOrderItems(itemDTOs);
        
        return dto;
    }

    private OrderItemDTO convertToItemDTO(OrderItem item) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setId(item.getId());
        dto.setProductId(item.getProduct().getId());
        dto.setQuantity(item.getQuantity());
        dto.setPrice(item.getPrice());
        return dto;
    }

    @Transactional
    public OrderDTO updateOrderStatus(Long id, Order.OrderStatus status, UserDetails userDetails) {
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + id));
        
        // Check if user has admin role
        boolean isAdmin = userDetails.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        
        if (!isAdmin) {
            throw new AccessDeniedException("Only admins can update order status");
        }
        
        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);
        messageProducer.sendOrderStatusChangedMessage(updatedOrder);
        
        return convertToDTO(updatedOrder);
    }

    private void validateStatusTransition(Order.OrderStatus currentStatus, Order.OrderStatus newStatus) {
        if (currentStatus == Order.OrderStatus.CANCELLED) {
            throw new IllegalStateException("Cannot update status of a cancelled order");
        }

        if (currentStatus == Order.OrderStatus.DELIVERED) {
            throw new IllegalStateException("Cannot update status of a delivered order");
        }

        if (newStatus == Order.OrderStatus.CANCELLED && currentStatus != Order.OrderStatus.PENDING) {
            throw new IllegalStateException("Can only cancel orders in PENDING status");
        }
    }
} 