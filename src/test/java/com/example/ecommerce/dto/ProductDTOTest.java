package com.example.ecommerce.dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductDTOTest {

    @Test
    void testProductDTO() {
        ProductDTO productDTO = new ProductDTO();
        
        productDTO.setId(1L);
        productDTO.setName("Test Product");
        productDTO.setDescription("Test Description");
        productDTO.setPrice(new BigDecimal("100.00"));
        productDTO.setStockQuantity(10);

        assertEquals(1L, productDTO.getId());
        assertEquals("Test Product", productDTO.getName());
        assertEquals("Test Description", productDTO.getDescription());
        assertEquals(new BigDecimal("100.00"), productDTO.getPrice());
        assertEquals(10, productDTO.getStockQuantity());
    }

    @Test
    void testProductDTOBuilder() {
        ProductDTO productDTO = ProductDTO.builder()
            .id(1L)
            .name("Test Product")
            .description("Test Description")
            .price(new BigDecimal("100.00"))
            .stockQuantity(10)
            .build();

        assertEquals(1L, productDTO.getId());
        assertEquals("Test Product", productDTO.getName());
        assertEquals("Test Description", productDTO.getDescription());
        assertEquals(new BigDecimal("100.00"), productDTO.getPrice());
        assertEquals(10, productDTO.getStockQuantity());
    }

    @Test
    void testProductDTOEquals() {
        ProductDTO productDTO1 = new ProductDTO();
        productDTO1.setId(1L);
        productDTO1.setName("Test Product");
        productDTO1.setDescription("Test Description");
        productDTO1.setPrice(new BigDecimal("100.00"));
        productDTO1.setStockQuantity(10);

        ProductDTO productDTO2 = new ProductDTO();
        productDTO2.setId(1L);
        productDTO2.setName("Test Product");
        productDTO2.setDescription("Test Description");
        productDTO2.setPrice(new BigDecimal("100.00"));
        productDTO2.setStockQuantity(10);

        assertEquals(productDTO1, productDTO2);
    }

    @Test
    void testProductDTOHashCode() {
        ProductDTO productDTO1 = new ProductDTO();
        productDTO1.setId(1L);
        productDTO1.setName("Test Product");
        productDTO1.setDescription("Test Description");
        productDTO1.setPrice(new BigDecimal("100.00"));
        productDTO1.setStockQuantity(10);

        ProductDTO productDTO2 = new ProductDTO();
        productDTO2.setId(1L);
        productDTO2.setName("Test Product");
        productDTO2.setDescription("Test Description");
        productDTO2.setPrice(new BigDecimal("100.00"));
        productDTO2.setStockQuantity(10);

        assertEquals(productDTO1.hashCode(), productDTO2.hashCode());
    }
} 