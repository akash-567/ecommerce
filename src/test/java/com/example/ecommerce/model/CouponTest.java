package com.example.ecommerce.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CouponTest {

    private Coupon coupon;

    @BeforeEach
    void setUp() {
        coupon = new Coupon();
        coupon.setId(1L);
        coupon.setCode("TEST10");
        coupon.setDiscountPercentage(new BigDecimal("10.00"));
        coupon.setMinPurchaseAmount(new BigDecimal("100.00"));
        coupon.setExpiryDate(LocalDateTime.now().plusDays(7));
        coupon.setMaxUses(100);
        coupon.setCurrentUses(0);
        coupon.setActive(true);
    }

    @Test
    void testCouponCreation() {
        assertNotNull(coupon);
        assertEquals(1L, coupon.getId());
        assertEquals("TEST10", coupon.getCode());
        assertEquals(new BigDecimal("10.00"), coupon.getDiscountPercentage());
        assertEquals(new BigDecimal("100.00"), coupon.getMinPurchaseAmount());
        assertNotNull(coupon.getExpiryDate());
        assertEquals(100, coupon.getMaxUses());
        assertEquals(0, coupon.getCurrentUses());
        assertTrue(coupon.isActive());
    }

    @Test
    void testSettersAndGetters() {
        Coupon newCoupon = new Coupon();
        
        newCoupon.setId(2L);
        assertEquals(2L, newCoupon.getId());

        newCoupon.setCode("TEST20");
        assertEquals("TEST20", newCoupon.getCode());

        newCoupon.setDiscountPercentage(new BigDecimal("20.00"));
        assertEquals(new BigDecimal("20.00"), newCoupon.getDiscountPercentage());

        newCoupon.setMinPurchaseAmount(new BigDecimal("200.00"));
        assertEquals(new BigDecimal("200.00"), newCoupon.getMinPurchaseAmount());

        LocalDateTime expiryDate = LocalDateTime.now().plusDays(14);
        newCoupon.setExpiryDate(expiryDate);
        assertEquals(expiryDate, newCoupon.getExpiryDate());

        newCoupon.setMaxUses(200);
        assertEquals(200, newCoupon.getMaxUses());

        newCoupon.setCurrentUses(50);
        assertEquals(50, newCoupon.getCurrentUses());

        newCoupon.setActive(false);
        assertFalse(newCoupon.isActive());
    }

    @Test
    void testDefaultValues() {
        Coupon newCoupon = new Coupon();
        assertEquals(0, newCoupon.getCurrentUses());
        assertTrue(newCoupon.isActive());
    }

    @Test
    void testCouponExpiry() {
        coupon.setExpiryDate(LocalDateTime.now().minusDays(1));
        assertTrue(coupon.getExpiryDate().isBefore(LocalDateTime.now()));
    }

    @Test
    void testCouponUsageLimit() {
        coupon.setCurrentUses(100);
        assertEquals(coupon.getMaxUses(), coupon.getCurrentUses());
    }

    @Test
    void testCouponDeactivation() {
        coupon.setActive(false);
        assertFalse(coupon.isActive());
    }

    @Test
    void testCouponDiscountCalculation() {
        assertEquals(new BigDecimal("10.00"), coupon.getDiscountPercentage());
    }

    @Test
    void testCouponMinPurchaseAmount() {
        assertEquals(new BigDecimal("100.00"), coupon.getMinPurchaseAmount());
    }
} 