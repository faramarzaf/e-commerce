package org.example.ecommerce.controller;

import lombok.RequiredArgsConstructor;
import org.example.ecommerce.dto.AddProductDto;
import org.example.ecommerce.dto.ResponseDto;
import org.example.ecommerce.dto.UpdateProductRequestDto;
import org.example.ecommerce.entity.Product;
import org.example.ecommerce.service.ProductService;
import org.example.ecommerce.utils.MyUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseDto<Product> addProduct(@RequestBody AddProductDto addProductDto) {
        return ResponseDto.success(MyUtils.MESSAGE_SUCCESS, productService.addProduct(addProductDto));
    }

    @GetMapping("/all")
    public ResponseDto<List<Product>> getAllProducts() {
        return ResponseDto.success(MyUtils.MESSAGE_SUCCESS, productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseDto<Product> getProductById(@PathVariable Long id) {
        return ResponseDto.success(MyUtils.MESSAGE_SUCCESS, productService.getProductById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update")
    public ResponseDto<Product> updateProduct(@RequestBody UpdateProductRequestDto updatedProduct) {
        return ResponseDto.success(MyUtils.MESSAGE_SUCCESS, productService.updateProduct(updatedProduct));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("delete/{id}")
    public ResponseDto<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseDto.success(MyUtils.MESSAGE_SUCCESS, "Product deleted successfully.");
    }

}
