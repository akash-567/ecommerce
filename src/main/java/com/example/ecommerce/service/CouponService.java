package com.example.ecommerce.service;

import com.example.ecommerce.model.Coupon;
import com.example.ecommerce.repository.CouponRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;

    public List<Coupon> getAllCoupons() {
        return couponRepository.findAll();
    }

    public Coupon getCoupon(Long id) {
        return couponRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Coupon not found"));
    }

    public Coupon createCoupon(Coupon coupon) {
        if (couponRepository.findByCode(coupon.getCode()).isPresent()) {
            throw new IllegalStateException("Coupon code already exists");
        }
        return couponRepository.save(coupon);
    }

    public Coupon updateCoupon(Long id, Coupon coupon) {
        Coupon existingCoupon = getCoupon(id);
        existingCoupon.setCode(coupon.getCode());
        existingCoupon.setDiscountPercentage(coupon.getDiscountPercentage());
        existingCoupon.setMinPurchaseAmount(coupon.getMinPurchaseAmount());
        existingCoupon.setExpiryDate(coupon.getExpiryDate());
        existingCoupon.setMaxUses(coupon.getMaxUses());
        existingCoupon.setActive(coupon.isActive());
        return couponRepository.save(existingCoupon);
    }

    public void deleteCoupon(Long id) {
        couponRepository.deleteById(id);
    }

    @Transactional
    public BigDecimal validateAndGetDiscount(String code, BigDecimal cartTotal) {
        Coupon coupon = couponRepository.findByCode(code)
            .orElseThrow(() -> new EntityNotFoundException("Coupon not found"));

        if (!coupon.isActive()) {
            throw new IllegalStateException("Coupon is not active");
        }

        if (coupon.getExpiryDate() != null && coupon.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Coupon has expired");
        }

        if (coupon.getMaxUses() != null && coupon.getCurrentUses() >= coupon.getMaxUses()) {
            throw new IllegalStateException("Coupon has reached maximum uses");
        }

        if (coupon.getMinPurchaseAmount() != null && cartTotal.compareTo(coupon.getMinPurchaseAmount()) < 0) {
            throw new IllegalStateException("Cart total does not meet minimum purchase amount");
        }

        coupon.setCurrentUses(coupon.getCurrentUses() + 1);
        couponRepository.save(coupon);

        // Calculate discount amount based on percentage
        return cartTotal.multiply(coupon.getDiscountPercentage())
            .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)
            .setScale(2, RoundingMode.HALF_UP);
    }

    public List<Coupon> getActiveCoupons() {
        return couponRepository.findByActiveTrueAndExpiryDateAfterOrExpiryDateIsNull(LocalDateTime.now());
    }
} 