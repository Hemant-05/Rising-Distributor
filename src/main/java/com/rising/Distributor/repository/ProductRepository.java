package com.rising.Distributor.repository;

import com.rising.Distributor.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, String> {
    Optional<Product> findByPid(String pid);
    List<Product> findByCategoryIgnoreCase(String category);
    long countByCategoryIgnoreCase(String category);
    List<Product> findByIsAvailableTrue();

    // Use the 'nameLower' field for efficient, case-insensitive searching
    @Query("SELECT p FROM Product p WHERE p.nameLower LIKE %:search%")
    List<Product> searchByName(@Param("search") String search);
}
