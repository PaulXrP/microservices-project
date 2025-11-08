package com.dev.pranay.productservice.Dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProductRequest {

    @NotNull(message = "Product name is required.")
    private String name;

    private String description;

    @NotNull(message = "sku Code is required.")
    private String skuCode;

    @NotNull(message = "Price is required.")
    @Positive(message = "Price must be positive.")
    private BigDecimal price;
}
