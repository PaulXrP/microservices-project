package com.dev.pranay.productservice.Dtos;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ProductResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID id;
    private String name;
    private String description;
    private String skuCode;
    private BigDecimal price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
