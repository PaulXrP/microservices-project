package com.dev.pranay.productservice.Services;

import com.dev.pranay.productservice.Dtos.ProductRequest;
import com.dev.pranay.productservice.Dtos.ProductResponse;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    ProductResponse createProduct(ProductRequest productRequest);

    ProductResponse getProductById(UUID uuid);

    List<ProductResponse> getAllProducts();

    ProductResponse updateProduct(UUID id, ProductRequest productRequest);

    void deleteProduct(UUID id);
}
