package com.dev.pranay.productservice.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "products")
@Data
@EntityListeners(AuditingEntityListener.class) // <--- CRITICAL: Don't forget this!
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false, unique = true)
    private String skuCode;

    @Column(nullable = false)
    private BigDecimal price;

//    @CreationTimestamp
    @CreatedDate // <--- Changed from @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

//    @UpdateTimestamp
    @LastModifiedDate // <--- Changed from @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
