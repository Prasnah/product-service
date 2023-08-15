package com.prasanna.products.entity;

import com.prasanna.products.dto.ProductAvailability;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Builder
public class PendingProduct {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long productId;

  @NonNull private String productName;
  @NonNull private double price;
  private ProductAvailability productAvailability;

  private Date productArrivalDate;
}
