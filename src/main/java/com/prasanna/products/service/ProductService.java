package com.prasanna.products.service;

import com.prasanna.products.entity.PendingProduct;
import com.prasanna.products.entity.Product;
import com.prasanna.products.repository.ApprovalRepository;
import com.prasanna.products.repository.ProductRepository;
import com.prasanna.products.config.MessagingConfig;
import com.prasanna.products.dto.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {
  public static final int PRODUCT_PRICE_LOWER_LIMIT = 5500;
  public static final int PRODUCT_PRICE_UPPER_LIMIT = 10000;
  @Autowired private RabbitTemplate template;
  @Autowired ProductRepository productRepository;

  @Autowired ApprovalRepository approvalRepository;

  public ProductStatus addProduct(final Product product) {
    ProductStatus productStatus;
    if (product.getPrice() > PRODUCT_PRICE_LOWER_LIMIT
        && product.getPrice() < PRODUCT_PRICE_UPPER_LIMIT) {
      productStatus =
          ProductStatus.builder()
              .product(product)
              .status(Status.PENDING)
              .message("Product waiting in queue for approval")
              .build();
      template.convertAndSend(MessagingConfig.EXCHANGE, MessagingConfig.ROUTING_KEY, productStatus);
    } else if (product.getPrice() > PRODUCT_PRICE_UPPER_LIMIT) {
      productStatus =
          ProductStatus.builder()
              .product(product)
              .status(Status.REJECTED)
              .message("Product rejected due to price constraints")
              .build();
    } else {
      productStatus =
          ProductStatus.builder()
              .product(product)
              .status(Status.APPROVED)
              .message("Product added successfully")
              .build();
      try {
        productRepository.save(product);
      } catch (Exception exception) {
        productStatus.setMessage(
            "An exception occurred during addition of product " + exception.getMessage());
      }
    }

    return productStatus;
  }

  public List<Product> getProducts() {
    return productRepository.findAll(Sort.by(Sort.Direction.ASC, "productArrivalDate"));
  }

  public List<Product> searchProducts(final ProductSearchCriteria productSearchCriteria) {
    List<Product> products = productRepository.findAll();
    products =
        products.stream()
            .filter(
                product ->
                    product
                                .getProductArrivalDate()
                                .compareTo(productSearchCriteria.getMinPostedDate())
                            < 1
                        && product
                                .getProductArrivalDate()
                                .compareTo(productSearchCriteria.getMaxPostedDate())
                            > 0
                        && product.getPrice() > productSearchCriteria.getMinPrice()
                        && product.getPrice() < productSearchCriteria.getMaxPrice())
            .collect(Collectors.toList());
    return products;
  }

  public Optional<Product> approveProductById(Long approvalId) {
    Optional<PendingProduct> pendingProductsForApproval = approvalRepository.findById(approvalId);
    if (pendingProductsForApproval.isPresent()) {
      PendingProduct pendingProduct = pendingProductsForApproval.get();
      Product product =
          Product.builder()
              .productName(pendingProduct.getProductName())
              .price(pendingProduct.getPrice())
              .productArrivalDate(pendingProduct.getProductArrivalDate())
              .productAvailability(pendingProduct.getProductAvailability())
              .build();
      productRepository.save(product);
      return Optional.of(product);
    }
    return Optional.empty();
  }
}
