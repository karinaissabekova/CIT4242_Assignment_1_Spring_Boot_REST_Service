package com.karina.onlinestore.service;

import com.karina.onlinestore.dto.CreateProductRequest;
import com.karina.onlinestore.dto.ProductResponse;
import com.karina.onlinestore.dto.UpdateProductRequest;
import com.karina.onlinestore.entity.Product;
import com.karina.onlinestore.exception.ProductNotFoundException;
import com.karina.onlinestore.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> findAll() {
        return productRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ProductResponse findById(Long id) {
        return toResponse(getProduct(id));
    }

    public ProductResponse create(CreateProductRequest request) {
        Product product = new Product(request.name(), request.description(), request.price(),
                request.category(), request.inStock());
        return toResponse(productRepository.save(product));
    }

    public ProductResponse update(Long id, UpdateProductRequest request) {
        Product product = getProduct(id);
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setCategory(request.category());
        product.setInStock(request.inStock() == null ? true : request.inStock());
        return toResponse(productRepository.save(product));
    }

    public void delete(Long id) {
        Product product = getProduct(id);
        productRepository.delete(product);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> findByCategory(String category) {
        return productRepository.findByCategoryIgnoreCase(category).stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> searchByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name == null ? "" : name)
                .stream().map(this::toResponse).toList();
    }

    private Product getProduct(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    }

    private ProductResponse toResponse(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getDescription(),
                product.getPrice(), product.getCategory(), product.getInStock(),
                product.getCreatedAt(), product.getUpdatedAt());
    }
}
