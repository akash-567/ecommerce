package com.example.ecommerce.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CartItemTest {

    private CartItem cartItem;
    private Cart cart;
    private Product product;

    @BeforeEach
    void setUp() {
        cart = new Cart();
        cart.setId(1L);

        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setPrice(new BigDecimal("100.00"));

        cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(2);
        cartItem.setPrice(new BigDecimal("100.00"));
    }

    @Test
    void testCartItemCreation() {
        assertNotNull(cartItem);
        assertEquals(1L, cartItem.getId());
        assertEquals(cart, cartItem.getCart());
        assertEquals(product, cartItem.getProduct());
        assertEquals(2, cartItem.getQuantity());
        assertEquals(new BigDecimal("100.00"), cartItem.getPrice());
    }

    @Test
    void testSubtotalCalculation() {
        assertEquals(new BigDecimal("200.00"), cartItem.getSubtotal());
    }

    @Test
    void testSettersAndGetters() {
        CartItem newCartItem = new CartItem();
        
        newCartItem.setId(2L);
        assertEquals(2L, newCartItem.getId());

        newCartItem.setCart(cart);
        assertEquals(cart, newCartItem.getCart());

        newCartItem.setProduct(product);
        assertEquals(product, newCartItem.getProduct());

        newCartItem.setQuantity(3);
        assertEquals(3, newCartItem.getQuantity());

        newCartItem.setPrice(new BigDecimal("150.00"));
        assertEquals(new BigDecimal("150.00"), newCartItem.getPrice());
    }

    @Test
    void testSubtotalWithZeroQuantity() {
        cartItem.setQuantity(0);
        assertEquals(new BigDecimal("0.00"), cartItem.getSubtotal());
    }

    @Test
    void testSubtotalWithZeroPrice() {
        cartItem.setPrice(BigDecimal.ZERO);
        assertEquals(new BigDecimal("0.00"), cartItem.getSubtotal());
    }
} 