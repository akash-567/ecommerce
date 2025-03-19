package com.example.ecommerce.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CartTest {

    private Cart cart;
    private User user;
    private CartItem cartItem;
    private Product product;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setPrice(new BigDecimal("100.00"));

        cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setProduct(product);
        cartItem.setQuantity(2);
        cartItem.setPrice(new BigDecimal("100.00"));

        cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);
        cart.setItems(new ArrayList<>());
        cart.getItems().add(cartItem);
        cartItem.setCart(cart);
    }

    @Test
    void testCartCreation() {
        assertNotNull(cart);
        assertEquals(1L, cart.getId());
        assertEquals(user, cart.getUser());
        assertEquals(1, cart.getItems().size());
        assertNull(cart.getAppliedCoupon());
        assertEquals(new BigDecimal("0.00"), cart.getDiscountAmount());
    }

    @Test
    void testTotalAmountCalculation() {
        assertEquals(new BigDecimal("200.00"), cart.getTotalAmount());
    }

    @Test
    void testFinalAmountCalculation() {
        cart.setDiscountAmount(new BigDecimal("20.00"));
        assertEquals(new BigDecimal("180.00"), cart.getFinalAmount());
    }

    @Test
    void testFinalAmountWithNoDiscount() {
        assertEquals(new BigDecimal("200.00"), cart.getFinalAmount());
    }

    @Test
    void testFinalAmountWithFullDiscount() {
        cart.setDiscountAmount(new BigDecimal("200.00"));
        assertEquals(new BigDecimal("0.00"), cart.getFinalAmount());
    }

    @Test
    void testSettersAndGetters() {
        Cart newCart = new Cart();
        
        newCart.setId(2L);
        assertEquals(2L, newCart.getId());

        newCart.setUser(user);
        assertEquals(user, newCart.getUser());

        List<CartItem> items = new ArrayList<>();
        newCart.setItems(items);
        assertEquals(items, newCart.getItems());

        newCart.setAppliedCoupon("TEST10");
        assertEquals("TEST10", newCart.getAppliedCoupon());

        newCart.setDiscountAmount(new BigDecimal("50.00"));
        assertEquals(new BigDecimal("50.00"), newCart.getDiscountAmount());
    }

    @Test
    void testEmptyCart() {
        cart.setItems(new ArrayList<>());
        assertEquals(new BigDecimal("0.00"), cart.getTotalAmount());
        assertEquals(new BigDecimal("0.00"), cart.getFinalAmount());
    }

    @Test
    void testCartWithMultipleItems() {
        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Test Product 2");
        product2.setPrice(new BigDecimal("50.00"));

        CartItem cartItem2 = new CartItem();
        cartItem2.setId(2L);
        cartItem2.setProduct(product2);
        cartItem2.setQuantity(3);
        cartItem2.setPrice(new BigDecimal("50.00"));
        cartItem2.setCart(cart);
        cart.getItems().add(cartItem2);

        assertEquals(new BigDecimal("350.00"), cart.getTotalAmount());
        assertEquals(new BigDecimal("350.00"), cart.getFinalAmount());
    }
} 