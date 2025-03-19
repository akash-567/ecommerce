package com.example.ecommerce.service;

import com.example.ecommerce.dto.OrderDTO;
import com.example.ecommerce.dto.OrderItemDTO;
import com.example.ecommerce.model.*;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.repository.UserRepository;
// import com.example.ecommerce.messaging.OrderMessageProducer;
// import com.example.ecommerce.messaging.OrderStatusMessage;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderMessageProducer orderMessageProducer;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private OrderService orderService;

    private User testUser;
    private Product testProduct;
    private Order testOrder;
    private OrderDTO testOrderDTO;
    private OrderItemDTO testOrderItemDTO;

    @BeforeEach
    void setUp() {
        // Setup test user
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");

        // Setup test product
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Test Product");
        testProduct.setPrice(new BigDecimal("100.00"));
        testProduct.setStockQuantity(10);

        // Setup test order item
        OrderItem testOrderItem = new OrderItem();
        testOrderItem.setId(1L);
        testOrderItem.setProduct(testProduct);
        testOrderItem.setQuantity(2);
        testOrderItem.setPrice(new BigDecimal("100.00"));

        // Setup test order
        testOrder = new Order();
        testOrder.setId(1L);
        testOrder.setOrderNumber("ORD-001");
        testOrder.setUser(testUser);
        testOrder.setOrderDate(LocalDateTime.now());
        testOrder.setStatus(Order.OrderStatus.PENDING);
        testOrder.setOrderItems(Arrays.asList(testOrderItem));
        testOrder.setTotalAmount(new BigDecimal("200.00"));

        // Setup test order item DTO
        testOrderItemDTO = new OrderItemDTO();
        testOrderItemDTO.setId(1L);
        testOrderItemDTO.setProductId(1L);
        testOrderItemDTO.setQuantity(2);
        testOrderItemDTO.setPrice(new BigDecimal("100.00"));

        // Setup test order DTO
        testOrderDTO = new OrderDTO();
        testOrderDTO.setId(1L);
        testOrderDTO.setOrderNumber("ORD-001");
        testOrderDTO.setOrderItems(Arrays.asList(testOrderItemDTO));
        testOrderDTO.setTotalAmount(new BigDecimal("200.00"));
        testOrderDTO.setStatus(Order.OrderStatus.PENDING);
    }

    @Test
    void createOrder_Success() {
        when(userDetails.getUsername()).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(testProduct));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        OrderDTO result = orderService.createOrder(testOrderDTO, userDetails);

        assertNotNull(result);
        assertEquals(testOrderDTO.getId(), result.getId());
        assertEquals(testOrderDTO.getOrderNumber(), result.getOrderNumber());
        verify(orderRepository).save(any(Order.class));
        verify(orderMessageProducer).sendOrderStatusUpdate(any(OrderStatusMessage.class));
    }

    @Test
    void createOrder_UserNotFound() {
        when(userDetails.getUsername()).thenReturn("nonexistent");
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> 
            orderService.createOrder(testOrderDTO, userDetails));
        verify(orderRepository, never()).save(any());
    }

    @Test
    void createOrder_ProductNotFound() {
        when(userDetails.getUsername()).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> 
            orderService.createOrder(testOrderDTO, userDetails));
        verify(orderRepository, never()).save(any());
    }

    @Test
    void createOrder_InsufficientStock() {
        when(userDetails.getUsername()).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(testProduct));
        testOrderItemDTO.setQuantity(20); // More than available stock

        assertThrows(IllegalStateException.class, () -> 
            orderService.createOrder(testOrderDTO, userDetails));
        verify(orderRepository, never()).save(any());
    }

    @Test
    void getUserOrders_Success() {
        when(userDetails.getUsername()).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(orderRepository.findByUser(testUser)).thenReturn(Arrays.asList(testOrder));

        List<OrderDTO> results = orderService.getUserOrders(userDetails);

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(testOrder.getId(), results.get(0).getId());
    }

    @Test
    void getUserOrders_UserNotFound() {
        when(userDetails.getUsername()).thenReturn("nonexistent");
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> 
            orderService.getUserOrders(userDetails));
        verify(orderRepository, never()).findByUser(any());
    }

    @Test
    void getOrder_Success() {
        when(userDetails.getUsername()).thenReturn("testuser");
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        OrderDTO result = orderService.getOrder(1L, userDetails);

        assertNotNull(result);
        assertEquals(testOrder.getId(), result.getId());
        assertEquals(testOrder.getOrderNumber(), result.getOrderNumber());
    }

    @Test
    void getOrder_NotFound() {
        Long orderId = 1L;
        when(userDetails.getUsername()).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> 
            orderService.getOrder(orderId, userDetails));
    }

    @Test
    void getOrder_Unauthorized() {
        when(userDetails.getUsername()).thenReturn("differentuser");
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        User differentUser = new User();
        differentUser.setId(2L);
        differentUser.setUsername("differentuser");
        when(userRepository.findByUsername("differentuser")).thenReturn(Optional.of(differentUser));

        assertThrows(SecurityException.class, () -> 
            orderService.getOrder(1L, userDetails));
    }

    @Test
    void updateOrderStatus_Success() {
        // Given
        Long orderId = 1L;
        Order.OrderStatus newStatus = Order.OrderStatus.CONFIRMED;

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);
        doReturn(Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN"))).when(userDetails).getAuthorities();

        // When
        OrderDTO updatedOrder = orderService.updateOrderStatus(orderId, newStatus, userDetails);

        // Then
        assertNotNull(updatedOrder);
        assertEquals(newStatus, updatedOrder.getStatus());
        verify(orderRepository).save(any(Order.class));
        verify(orderMessageProducer).sendOrderStatusChangedMessage(any(Order.class));
    }

    @Test
    void updateOrderStatus_NotFound() {
        Long orderId = 1L;
        Order.OrderStatus newStatus = Order.OrderStatus.CONFIRMED;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> 
            orderService.updateOrderStatus(orderId, newStatus, userDetails));
    }

    @Test
    void updateOrderStatus_Unauthorized() {
        // Given
        Long orderId = 1L;
        Order.OrderStatus newStatus = Order.OrderStatus.CONFIRMED;

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(testOrder));
        doReturn(Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))).when(userDetails).getAuthorities();

        // When/Then
        assertThrows(AccessDeniedException.class, () -> 
            orderService.updateOrderStatus(orderId, newStatus, userDetails));
        verify(orderRepository, never()).save(any(Order.class));
        verify(orderMessageProducer, never()).sendOrderStatusChangedMessage(any(Order.class));
    }
} 