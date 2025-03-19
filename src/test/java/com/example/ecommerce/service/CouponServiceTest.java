package com.example.ecommerce.service;

import com.example.ecommerce.model.Coupon;
import com.example.ecommerce.repository.CouponRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouponServiceTest {

    @Mock
    private CouponRepository couponRepository;

    @InjectMocks
    private CouponService couponService;

    private Coupon testCoupon;

    @BeforeEach
    void setUp() {
        testCoupon = new Coupon();
        testCoupon.setId(1L);
        testCoupon.setCode("TEST10");
        testCoupon.setDiscountPercentage(new BigDecimal("10.00"));
        testCoupon.setMinPurchaseAmount(new BigDecimal("100.00"));
        testCoupon.setExpiryDate(LocalDateTime.now().plusDays(7));
        testCoupon.setMaxUses(100);
        testCoupon.setCurrentUses(0);
        testCoupon.setActive(true);
    }

    @Test
    void getAllCoupons_Success() {
        List<Coupon> expectedCoupons = Arrays.asList(testCoupon);
        when(couponRepository.findAll()).thenReturn(expectedCoupons);

        List<Coupon> result = couponService.getAllCoupons();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testCoupon, result.get(0));
    }

    @Test
    void getCoupon_Success() {
        when(couponRepository.findById(1L)).thenReturn(Optional.of(testCoupon));

        Coupon result = couponService.getCoupon(1L);

        assertNotNull(result);
        assertEquals(testCoupon.getCode(), result.getCode());
        assertEquals(testCoupon.getDiscountPercentage(), result.getDiscountPercentage());
    }

    @Test
    void getCoupon_NotFound() {
        when(couponRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> 
            couponService.getCoupon(1L));
    }

    @Test
    void createCoupon_Success() {
        when(couponRepository.findByCode("TEST10")).thenReturn(Optional.empty());
        when(couponRepository.save(any(Coupon.class))).thenReturn(testCoupon);

        Coupon result = couponService.createCoupon(testCoupon);

        assertNotNull(result);
        assertEquals(testCoupon.getCode(), result.getCode());
        assertEquals(testCoupon.getDiscountPercentage(), result.getDiscountPercentage());
        verify(couponRepository).save(any(Coupon.class));
    }

    @Test
    void createCoupon_DuplicateCode() {
        when(couponRepository.findByCode("TEST10")).thenReturn(Optional.of(testCoupon));

        assertThrows(IllegalStateException.class, () -> 
            couponService.createCoupon(testCoupon));
    }

    @Test
    void updateCoupon_Success() {
        Coupon updatedCoupon = new Coupon();
        updatedCoupon.setDiscountPercentage(new BigDecimal("15.00"));
        updatedCoupon.setMinPurchaseAmount(new BigDecimal("200.00"));
        updatedCoupon.setExpiryDate(LocalDateTime.now().plusDays(14));
        updatedCoupon.setMaxUses(200);
        updatedCoupon.setActive(true);

        when(couponRepository.findById(1L)).thenReturn(Optional.of(testCoupon));
        when(couponRepository.save(any(Coupon.class))).thenReturn(testCoupon);

        Coupon result = couponService.updateCoupon(1L, updatedCoupon);

        assertNotNull(result);
        assertEquals(new BigDecimal("15.00"), result.getDiscountPercentage());
        assertEquals(new BigDecimal("200.00"), result.getMinPurchaseAmount());
        assertEquals(updatedCoupon.getExpiryDate(), result.getExpiryDate());
        assertEquals(200, result.getMaxUses());
        assertTrue(result.isActive());
    }

    @Test
    void deleteCoupon_Success() {
        couponService.deleteCoupon(1L);

        verify(couponRepository).deleteById(1L);
    }

    @Test
    void validateAndGetDiscount_Success() {
        when(couponRepository.findByCode("TEST10")).thenReturn(Optional.of(testCoupon));

        BigDecimal cartTotal = new BigDecimal("200.00");
        BigDecimal result = couponService.validateAndGetDiscount("TEST10", cartTotal);

        assertEquals(new BigDecimal("20.00"), result);
    }

    @Test
    void validateAndGetDiscount_InactiveCoupon() {
        testCoupon.setActive(false);
        when(couponRepository.findByCode("TEST10")).thenReturn(Optional.of(testCoupon));

        assertThrows(IllegalStateException.class, () -> 
            couponService.validateAndGetDiscount("TEST10", new BigDecimal("200.00")));
    }

    @Test
    void validateAndGetDiscount_ExpiredCoupon() {
        testCoupon.setExpiryDate(LocalDateTime.now().minusDays(1));
        when(couponRepository.findByCode("TEST10")).thenReturn(Optional.of(testCoupon));

        assertThrows(IllegalStateException.class, () -> 
            couponService.validateAndGetDiscount("TEST10", new BigDecimal("200.00")));
    }

    @Test
    void validateAndGetDiscount_MaxUsesReached() {
        testCoupon.setCurrentUses(100);
        when(couponRepository.findByCode("TEST10")).thenReturn(Optional.of(testCoupon));

        assertThrows(IllegalStateException.class, () -> 
            couponService.validateAndGetDiscount("TEST10", new BigDecimal("200.00")));
    }

    @Test
    void validateAndGetDiscount_MinPurchaseAmountNotMet() {
        when(couponRepository.findByCode("TEST10")).thenReturn(Optional.of(testCoupon));

        assertThrows(IllegalStateException.class, () -> 
            couponService.validateAndGetDiscount("TEST10", new BigDecimal("50.00")));
    }

    @Test
    void getActiveCoupons_Success() {
        List<Coupon> expectedCoupons = Arrays.asList(testCoupon);
        when(couponRepository.findByActiveTrueAndExpiryDateAfterOrExpiryDateIsNull(any(LocalDateTime.class)))
            .thenReturn(expectedCoupons);

        List<Coupon> result = couponService.getActiveCoupons();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testCoupon, result.get(0));
    }
} 