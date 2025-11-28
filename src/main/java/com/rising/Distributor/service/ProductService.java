package com.rising.Distributor.service;

import com.rising.Distributor.exception.ResourceNotFoundException;
import com.rising.Distributor.model.Product;
import com.rising.Distributor.repository.ProductRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product addProduct(Product product) {
        if (product.getPid() == null || product.getPid().isEmpty()) {
            product.setPid(generatePid(product.getCategory()));
        }
        product.setNameLower(product.getName().toLowerCase());
        return productRepository.save(product);
    }

    public Product updateProduct(String pid, Product productDetails) {
        Product existingProduct = productRepository.findByPid(pid)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with PID: " + pid));
        
        existingProduct.setName(productDetails.getName());
        existingProduct.setNameLower(productDetails.getName().toLowerCase());
        existingProduct.setCategory(productDetails.getCategory());
        existingProduct.setPrice(productDetails.getPrice());
        existingProduct.setDescription(productDetails.getDescription());
        existingProduct.setStockQuantity(productDetails.getStockQuantity());
        existingProduct.setIsAvailable(productDetails.getIsAvailable());
        existingProduct.setPhotosList(productDetails.getPhotosList());
        return productRepository.save(existingProduct);
    }
    
    public void deleteProduct(String pid) {
        if (!productRepository.existsById(pid)) {
            throw new ResourceNotFoundException("Product not found with PID: " + pid);
        }
        productRepository.deleteById(pid);
    }

    public List<Product> getAllProducts() {
        return productRepository.findByIsAvailableTrue();
    }

    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryIgnoreCase(category);
    }

    public Product getProductByPid(String pid) {
        return productRepository.findByPid(pid)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with PID: " + pid));
    }

    public List<Product> searchProducts(String query) {
        return productRepository.searchByName(query.toLowerCase());
    }

    private String generatePid(String category) {
        String prefix = category.substring(0, 3).toUpperCase();
        long count = productRepository.countByCategoryIgnoreCase(category) + 1;
        return prefix + String.format("%03d", count);
    }
}
