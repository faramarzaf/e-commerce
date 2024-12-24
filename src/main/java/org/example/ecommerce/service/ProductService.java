package org.example.ecommerce.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.ecommerce.dto.AddProductDto;
import org.example.ecommerce.dto.UpdateProductRequestDto;
import org.example.ecommerce.entity.Product;
import org.example.ecommerce.exceptions.NotFoundException;
import org.example.ecommerce.repo.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {


    private final ProductRepository productRepository;

    @Transactional
    public Product addProduct(AddProductDto productDto) {
        Product product = new Product();
        product.setName(productDto.name());
        product.setDescription(productDto.description());
        product.setPrice(productDto.price());
        product.setStockQuantity(productDto.stockQuantity());
        product.setCategory(productDto.category().toUpperCase());
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found with id " + id));
    }

    @Transactional
    public Product updateProduct(UpdateProductRequestDto requestDto) {
        Product product = productRepository.findById(requestDto.id())
                .orElseThrow(() -> new NotFoundException("Product not found with id " + requestDto.id()));

        if (!ObjectUtils.isEmpty(requestDto.name()))
            product.setName(requestDto.name());

        if (!ObjectUtils.isEmpty(requestDto.price()))
            product.setPrice(requestDto.price());

        if (!ObjectUtils.isEmpty(requestDto.stock()))
            product.setStockQuantity(requestDto.stock());

        if (!ObjectUtils.isEmpty(requestDto.category()))
            product.setCategory(requestDto.category().toUpperCase());

        return productRepository.save(product);
    }


    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new NotFoundException("Product not found with id " + id);
        }
        productRepository.deleteById(id);
    }

}
