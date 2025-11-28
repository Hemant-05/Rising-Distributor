package com.rising.Distributor.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Product {

    @Id
    @Column(name = "pid", unique = true)
    private String pid; // Product ID - This is the true Primary Key

    @Column(name = "uid")
    private String uid; // Admin's UID - for tracking who added the product

    @Column(nullable = false)
    private String name;

    @Column(name = "name_lower")
    private String nameLower;

    private Double rating = 0.0;

    @Column(nullable = false)
    private String category;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    private Integer quantity;

    private String measurement;

    @Column(name = "mrp", precision = 10, scale = 2)
    private BigDecimal mrp;

    @Column(name = "stock_quantity")
    private Integer stockQuantity;

    @Column(name = "low_stock_quantity")
    private Integer lowStockQuantity;

    @Column(name = "last_stock_update")
    private LocalDateTime lastStockUpdate;

    @Column(name = "is_available")
    private Boolean isAvailable = true;

    @ElementCollection
    @CollectionTable(name = "product_photos", joinColumns = @JoinColumn(name = "product_pid"))
    @Column(name = "photo_url")
    private List<String> photosList = new ArrayList<>();
}
