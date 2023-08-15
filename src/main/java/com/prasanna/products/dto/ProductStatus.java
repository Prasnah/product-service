package com.prasanna.products.dto;

import com.prasanna.products.entity.Product;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ProductStatus {

  private Product product;
  private Status status;
  private String message;
}
