package com.prasanna.products.controller;

import com.prasanna.products.entity.Product;
import com.prasanna.products.dto.ProductSearchCriteria;
import com.prasanna.products.dto.ProductStatus;
import com.prasanna.products.service.ProductService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

  private final ProductService productService;

  @Autowired
  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @PostMapping
  public ResponseEntity<ProductStatus> addProduct(@RequestBody final Product product) {
    return new ResponseEntity<>(this.productService.addProduct(product), HttpStatus.OK);
  }

  @GetMapping("/getProducts")
  public ResponseEntity<List<Product>> getProducts() {
    return new ResponseEntity<>(this.productService.getProducts(), HttpStatus.OK);
  }

  @GetMapping("/search")
  public ResponseEntity<List<Product>> searchProducts(
      final ProductSearchCriteria productSearchCriteria) {
    return new ResponseEntity<>(
        this.productService.searchProducts(productSearchCriteria), HttpStatus.OK);
  }

  @SneakyThrows
  @PutMapping("/approval-queue/{approvalId}/approve")
  public ResponseEntity<Product> approveProductsById(@PathVariable Long approvalId) {
    return new ResponseEntity<>(
        this.productService.approveProductById(approvalId).get(), HttpStatus.ACCEPTED);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleGeneralException() {
    return new ResponseEntity<>("General Exception occured", HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
