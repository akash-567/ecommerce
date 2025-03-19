package com.example.ecommerce.service;

import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.CartItem;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.CartRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CouponService couponService;

    public Cart getCart(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("User not found"));
        
        return cartRepository.findByUser(user)
            .orElseGet(() -> createNewCart(user));
    }

    @Transactional
    public Cart addItemToCart(Long userId, Long productId, Integer quantity) {
        Cart cart = getCart(userId);
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        if (product.getStockQuantity() < quantity) {
            throw new IllegalStateException("Insufficient stock available");
        }

        Optional<CartItem> existingItem = cart.getItems().stream()
            .filter(item -> item.getProduct().getId().equals(productId))
            .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            newItem.setPrice(product.getPrice());
            cart.getItems().add(newItem);
        }

        return cartRepository.save(cart);
    }

    @Transactional
    public Cart updateCartItemQuantity(Long userId, Long productId, Integer quantity) {
        Cart cart = getCart(userId);
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        if (product.getStockQuantity() < quantity) {
            throw new IllegalStateException("Insufficient stock available");
        }

        cart.getItems().stream()
            .filter(item -> item.getProduct().getId().equals(productId))
            .findFirst()
            .ifPresent(item -> {
                if (quantity <= 0) {
                    cart.getItems().remove(item);
                } else {
                    item.setQuantity(quantity);
                }
            });

        return cartRepository.save(cart);
    }

    @Transactional
    public Cart removeItemFromCart(Long userId, Long productId) {
        Cart cart = getCart(userId);
        cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
        return cartRepository.save(cart);
    }

    @Transactional
    public Cart clearCart(Long userId) {
        Cart cart = getCart(userId);
        cart.getItems().clear();
        return cartRepository.save(cart);
    }

    @Transactional
    public Cart applyCoupon(Long userId, String couponCode) {
        Cart cart = getCart(userId);
        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cannot apply coupon to empty cart");
        }

        BigDecimal cartTotal = cart.getTotalAmount();
        BigDecimal discount = couponService.validateAndGetDiscount(couponCode, cartTotal);
        cart.setAppliedCoupon(couponCode);
        cart.setDiscountAmount(discount);
        return cartRepository.save(cart);
    }

    @Transactional
    public Cart removeCoupon(Long userId) {
        Cart cart = getCart(userId);
        cart.setAppliedCoupon(null);
        cart.setDiscountAmount(BigDecimal.ZERO);
        return cartRepository.save(cart);
    }

    private Cart createNewCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        return cartRepository.save(cart);
    }
} 