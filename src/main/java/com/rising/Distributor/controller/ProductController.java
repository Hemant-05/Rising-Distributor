package com.rising.Distributor.controller;

import com.rising.Distributor.dto.ApiResponse;
import com.rising.Distributor.model.Product;
import com.rising.Distributor.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Product>>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ApiResponse.success(HttpStatus.OK, "Products retrieved successfully", products);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<List<Product>>> getProductsByCategory(@PathVariable String category) {
        List<Product> products = productService.getProductsByCategory(category);
        return ApiResponse.success(HttpStatus.OK, "Products retrieved successfully", products);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<Product>>> searchProducts(@RequestParam String q) {
        List<Product> products = productService.searchProducts(q);
        return ApiResponse.success(HttpStatus.OK, "Products retrieved successfully", products);
    }

    @GetMapping("/{pid}")
    public ResponseEntity<ApiResponse<Product>> getProduct(@PathVariable String pid) {
        Product product = productService.getProductByPid(pid);
        return ApiResponse.success(HttpStatus.OK, "Product retrieved successfully", product);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Product>> addProduct(@RequestBody Product product, Authentication authentication) {
        String adminUid = authentication.getName();
        product.setUid(adminUid);
        Product newProduct = productService.addProduct(product);
        return ApiResponse.success(HttpStatus.CREATED, "Product added successfully", newProduct);
    }

    @PutMapping("/{pid}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Product>> updateProduct(@PathVariable String pid, @RequestBody Product product) {
        Product updatedProduct = productService.updateProduct(pid, product);
        return ApiResponse.success(HttpStatus.OK, "Product updated successfully", updatedProduct);
    }

    @DeleteMapping("/{pid}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable String pid) {
        productService.deleteProduct(pid);
        return ApiResponse.success(HttpStatus.OK, "Product deleted successfully");
    }
}
