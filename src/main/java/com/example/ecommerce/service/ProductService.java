package com.example.ecommerce.service;

import com.example.ecommerce.dto.ProductDTO;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    public Page<ProductDTO> searchProducts(String keyword, Pageable pageable) {
        if (!StringUtils.hasText(keyword)) {
            return getAllProducts(pageable);
        }
        return productRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword, pageable)
                .map(this::convertToDTO);
    }

    @Transactional(readOnly = true)
    public ProductDTO getProduct(Long id) {
        return productRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
    }

    @Transactional
    public ProductDTO createProduct(ProductDTO productDTO) {
        validateProduct(productDTO);
        Product product = convertToEntity(productDTO);
        Product savedProduct = productRepository.save(product);
        return convertToDTO(savedProduct);
    }

    @Transactional
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        validateProduct(productDTO);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
        
        updateProductFromDTO(product, productDTO);
        Product updatedProduct = productRepository.save(product);
        return convertToDTO(updatedProduct);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
        productRepository.deleteById(id);
    }

    private void validateProduct(ProductDTO productDTO) {
        if (!StringUtils.hasText(productDTO.getName())) {
            throw new IllegalStateException("Product name is required");
        }

        if (productDTO.getPrice() == null || productDTO.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("Product price must be greater than zero");
        }

        if (productDTO.getStockQuantity() < 0) {
            throw new IllegalStateException("Stock quantity cannot be negative");
        }
    }

    private ProductDTO convertToDTO(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .imageUrl(product.getImageUrl())
                .stockQuantity(product.getStockQuantity())
                .build();
    }

    private Product convertToEntity(ProductDTO dto) {
        Product product = new Product();
        updateProductFromDTO(product, dto);
        return product;
    }

    private void updateProductFromDTO(Product product, ProductDTO dto) {
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setImageUrl(dto.getImageUrl());
        product.setStockQuantity(dto.getStockQuantity());
    }
}