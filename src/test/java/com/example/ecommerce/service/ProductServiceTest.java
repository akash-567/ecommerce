package com.example.ecommerce.service;

import com.example.ecommerce.dto.ProductDTO;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product testProduct;
    private ProductDTO testProductDTO;

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Test Product");
        testProduct.setDescription("Test Description");
        testProduct.setPrice(new BigDecimal("100.00"));
        testProduct.setStockQuantity(10);

        testProductDTO = new ProductDTO();
        testProductDTO.setId(1L);
        testProductDTO.setName("Test Product");
        testProductDTO.setDescription("Test Description");
        testProductDTO.setPrice(new BigDecimal("100.00"));
        testProductDTO.setStockQuantity(10);
    }

    @Test
    void createProduct_Success() {
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        ProductDTO result = productService.createProduct(testProductDTO);

        assertNotNull(result);
        assertEquals(testProductDTO.getId(), result.getId());
        assertEquals(testProductDTO.getName(), result.getName());
        assertEquals(testProductDTO.getPrice(), result.getPrice());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void getAllProducts_Success() {
        List<Product> products = Arrays.asList(testProduct);
        Page<Product> page = new PageImpl<>(products, PageRequest.of(0, 10), products.size());
        when(productRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<ProductDTO> result = productService.getAllProducts(PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(testProductDTO.getPrice(), result.getContent().get(0).getPrice());
    }

    @Test
    void getProduct_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        ProductDTO result = productService.getProduct(1L);

        assertNotNull(result);
        assertEquals(testProductDTO.getId(), result.getId());
        assertEquals(testProductDTO.getName(), result.getName());
        assertEquals(testProductDTO.getPrice(), result.getPrice());
    }

    @Test
    void getProduct_NotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> productService.getProduct(1L));
    }

    @Test
    void updateProduct_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        ProductDTO result = productService.updateProduct(1L, testProductDTO);

        assertNotNull(result);
        assertEquals(testProductDTO.getId(), result.getId());
        assertEquals(testProductDTO.getName(), result.getName());
        assertEquals(testProductDTO.getPrice(), result.getPrice());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void updateProduct_NotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> productService.updateProduct(1L, testProductDTO));
    }

    @Test
    void deleteProduct_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        productService.deleteProduct(1L);

        verify(productRepository).deleteById(1L);
    }

    @Test
    void deleteProduct_NotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> productService.deleteProduct(1L));
        verify(productRepository, never()).deleteById(any());
    }
} 