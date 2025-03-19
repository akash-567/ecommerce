package com.example.ecommerce.controller;

import com.example.ecommerce.dto.ProductDTO;
import com.example.ecommerce.service.ProductService;
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
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private ProductDTO testProductDTO;

    @BeforeEach
    void setUp() {
        testProductDTO = new ProductDTO();
        testProductDTO.setId(1L);
        testProductDTO.setName("Test Product");
        testProductDTO.setDescription("Test Description");
        testProductDTO.setPrice(new BigDecimal("100.00"));
        testProductDTO.setStockQuantity(10);
    }

    @Test
    void getAllProducts_Success() {
        List<ProductDTO> products = Arrays.asList(testProductDTO);
        Page<ProductDTO> page = new PageImpl<>(products, PageRequest.of(0, 10), products.size());
        when(productService.getAllProducts(any(Pageable.class))).thenReturn(page);

        ResponseEntity<Page<ProductDTO>> response = productController.getAllProducts(PageRequest.of(0, 10));

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().getContent().size());
    }

    @Test
    void createProduct_Success() {
        when(productService.createProduct(any(ProductDTO.class))).thenReturn(testProductDTO);

        ResponseEntity<ProductDTO> response = productController.createProduct(testProductDTO);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(testProductDTO, response.getBody());
    }

    @Test
    void getProduct_Success() {
        when(productService.getProduct(1L)).thenReturn(testProductDTO);

        ResponseEntity<ProductDTO> response = productController.getProduct(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(testProductDTO, response.getBody());
    }

    @Test
    void updateProduct_Success() {
        when(productService.updateProduct(any(Long.class), any(ProductDTO.class))).thenReturn(testProductDTO);

        ResponseEntity<ProductDTO> response = productController.updateProduct(1L, testProductDTO);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(testProductDTO, response.getBody());
    }

    @Test
    void deleteProduct_Success() {
        ResponseEntity<Void> response = productController.deleteProduct(1L);

        assertNotNull(response);
        assertEquals(204, response.getStatusCodeValue());
    }
} 