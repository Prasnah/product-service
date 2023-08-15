package com.prasanna.products.dto;

import jakarta.annotation.Nullable;
import lombok.Data;

import java.util.Date;

@Data
public class ProductSearchCriteria {
  @Nullable private String productName;
  @Nullable private double minPrice;
  @Nullable private double maxPrice;
  @Nullable private Date minPostedDate;
  @Nullable private Date maxPostedDate;
}
