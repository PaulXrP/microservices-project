package com.dev.pranay.productservice.Services.Impl;

import com.dev.pranay.productservice.Dtos.ProductRequest;
import com.dev.pranay.productservice.Dtos.ProductResponse;
import com.dev.pranay.productservice.Entities.Product;
import com.dev.pranay.productservice.Exceptions.ResourceNotFoundException;
import com.dev.pranay.productservice.Repositories.ProductRepository;
import com.dev.pranay.productservice.Services.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    @Transactional
    @CacheEvict(value = "productList", allEntries = true) // <--- CLEAR THE CACHE
    public ProductResponse createProduct(ProductRequest productRequest) {

        log.info("Attempting to create product with SKU: {}", productRequest.getSkuCode());

        // Optional: Check if SKU already exists
        productRepository.findBySkuCode(productRequest.getSkuCode())
                .ifPresent(p -> {
                    log.warn("Product with {} already exist.",  productRequest.getSkuCode());
                    throw new IllegalArgumentException("Product with SKU code " + p.getSkuCode() + " already exists.");
                });

        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .skuCode(productRequest.getSkuCode())
                .price(productRequest.getPrice())
                .build();

        Product savedProduct = productRepository.save(product);
        log.info("Product created successfully with ID {} ", savedProduct.getId());

        return mapToProductResponse(savedProduct);
    }

    // This mapping logic would be in a dedicated Mapper class in a larger app
    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .skuCode(product.getSkuCode())
                .price(product.getPrice())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(UUID uuid) {
        log.info("Fetching product with ID: {} :", uuid);
        Product product = productRepository.findById(uuid).orElseThrow(() -> {
            log.warn("Product not found with ID: {}", uuid);
            return new ResourceNotFoundException("Product not found with ID: " + uuid);
        });

        return mapToProductResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "productList")
    public List<ProductResponse> getAllProducts() {
        log.info("Fetching all products from DATABASE"); // This log proves we hit the DB
        return productRepository.findAll()
                .stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @CacheEvict(value = "productList", allEntries = true) // <--- CLEAR THE CACHE
    public ProductResponse updateProduct(UUID id, ProductRequest productRequest) {
        log.info("Attempting to update product with ID: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Product with ID {}not found: ", id);
                    return new ResourceNotFoundException("Product not found with ID: " + id);
                });

        // Check if the SKU is being updated and if the new SKU already exists for a *different* product
        if(!product.getSkuCode().equals(productRequest.getSkuCode())) {
            productRepository.findBySkuCode(productRequest.getSkuCode()).ifPresent(exisitngProduct -> {
                if(!exisitngProduct.getId().equals(id)) {
                    log.warn("Product with SKU {} already exists.", productRequest.getSkuCode());
                    throw new IllegalArgumentException("Product with SKU code " + productRequest.getSkuCode() + " already exists.");
                }
            });
        }

        // Update the product fields
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setSkuCode(productRequest.getSkuCode());
        product.setPrice(productRequest.getPrice());
        // 'updatedAt' will be handled automatically by @UpdateTimestamp

        Product updatedProduct = productRepository.save(product);
        log.info("Product updated successfully with ID: {}", updatedProduct.getId());

        return mapToProductResponse(updatedProduct);
    }

    @Override
    @Transactional
    @CacheEvict(value = "productList", allEntries = true) // <--- CLEAR THE CACHE
    public void deleteProduct(UUID id) {
        log.info("Attempting to delete product with ID: {}", id);

        Product product = productRepository.findById(id).orElseThrow(() -> {
            log.warn("Product not found with ID: {}", id);
            return new ResourceNotFoundException("Product not found with ID: " + id);
        });

        productRepository.delete(product);
        log.info("Product deleted successfully with ID: {}", id);
    }
}
