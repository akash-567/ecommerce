package com.example.ecommerce.service;

import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.CartItem;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.CartRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CouponService couponService;

    @InjectMocks
    private CartService cartService;

    private User testUser;
    private Product testProduct;
    private Cart testCart;
    private CartItem testCartItem;

    @BeforeEach
    void setUp() {
        // Setup test user
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");

        // Setup test product
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Test Product");
        testProduct.setPrice(new BigDecimal("100.00"));
        testProduct.setStockQuantity(10);

        // Setup test cart item
        testCartItem = new CartItem();
        testCartItem.setId(1L);
        testCartItem.setProduct(testProduct);
        testCartItem.setQuantity(2);
        testCartItem.setPrice(new BigDecimal("100.00"));

        // Setup test cart
        testCart = new Cart();
        testCart.setId(1L);
        testCart.setUser(testUser);
        testCart.setItems(new ArrayList<>());
        testCart.getItems().add(testCartItem);
        testCartItem.setCart(testCart);
    }

    @Test
    void getCart_ExistingCart_ReturnsCart() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(cartRepository.findByUser(testUser)).thenReturn(Optional.of(testCart));

        Cart result = cartService.getCart(1L);

        assertNotNull(result);
        assertEquals(testCart.getId(), result.getId());
        assertEquals(testCart.getUser(), result.getUser());
        assertEquals(1, result.getItems().size());
    }

    @Test
    void getCart_NonExistingCart_CreatesNewCart() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(cartRepository.findByUser(testUser)).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenAnswer(i -> i.getArgument(0));

        Cart result = cartService.getCart(1L);

        assertNotNull(result);
        assertEquals(testUser, result.getUser());
        assertTrue(result.getItems().isEmpty());
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void addItemToCart_NewProduct_AddsItem() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(cartRepository.findByUser(testUser)).thenReturn(Optional.of(testCart));
        when(productRepository.findById(2L)).thenReturn(Optional.of(testProduct));
        when(cartRepository.save(any(Cart.class))).thenAnswer(i -> i.getArgument(0));

        Cart result = cartService.addItemToCart(1L, 2L, 3);

        assertNotNull(result);
        assertEquals(2, result.getItems().size());
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void addItemToCart_ExistingProduct_UpdatesQuantity() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(cartRepository.findByUser(testUser)).thenReturn(Optional.of(testCart));
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(cartRepository.save(any(Cart.class))).thenAnswer(i -> i.getArgument(0));

        Cart result = cartService.addItemToCart(1L, 1L, 3);

        assertNotNull(result);
        assertEquals(5, testCartItem.getQuantity()); // 2 + 3 = 5
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void updateCartItemQuantity_ExistingItem_UpdatesQuantity() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(cartRepository.findByUser(testUser)).thenReturn(Optional.of(testCart));
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(cartRepository.save(any(Cart.class))).thenAnswer(i -> i.getArgument(0));

        Cart result = cartService.updateCartItemQuantity(1L, 1L, 5);

        assertNotNull(result);
        assertEquals(5, testCartItem.getQuantity());
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void updateCartItemQuantity_NonExistingItem_ThrowsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(cartRepository.findByUser(testUser)).thenReturn(Optional.of(testCart));
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> 
            cartService.updateCartItemQuantity(1L, 99L, 5));
        verify(cartRepository, never()).save(any());
    }

    @Test
    void removeItemFromCart_ExistingItem_RemovesItem() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(cartRepository.findByUser(testUser)).thenReturn(Optional.of(testCart));
        when(cartRepository.save(any(Cart.class))).thenAnswer(i -> i.getArgument(0));

        Cart result = cartService.removeItemFromCart(1L, 1L);

        assertNotNull(result);
        assertTrue(result.getItems().isEmpty());
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void clearCart_RemovesAllItems() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(cartRepository.findByUser(testUser)).thenReturn(Optional.of(testCart));
        when(cartRepository.save(any(Cart.class))).thenAnswer(i -> i.getArgument(0));

        Cart result = cartService.clearCart(1L);

        assertNotNull(result);
        assertTrue(result.getItems().isEmpty());
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void applyCoupon_ValidCoupon_AppliesCoupon() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(cartRepository.findByUser(testUser)).thenReturn(Optional.of(testCart));
        when(couponService.validateAndGetDiscount("TEST10", testCart.getTotalAmount()))
            .thenReturn(new BigDecimal("20.00"));
        when(cartRepository.save(any(Cart.class))).thenAnswer(i -> i.getArgument(0));

        Cart result = cartService.applyCoupon(1L, "TEST10");

        assertNotNull(result);
        assertEquals("TEST10", result.getAppliedCoupon());
        assertEquals(new BigDecimal("20.00"), result.getDiscountAmount());
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void removeCoupon_RemovesCoupon() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(cartRepository.findByUser(testUser)).thenReturn(Optional.of(testCart));
        when(cartRepository.save(any(Cart.class))).thenAnswer(i -> i.getArgument(0));

        Cart result = cartService.removeCoupon(1L);

        assertNotNull(result);
        assertNull(result.getAppliedCoupon());
        assertEquals(BigDecimal.ZERO, result.getDiscountAmount());
        verify(cartRepository).save(any(Cart.class));
    }
} 