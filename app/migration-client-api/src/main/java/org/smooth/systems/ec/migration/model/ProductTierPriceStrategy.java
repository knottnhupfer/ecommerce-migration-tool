package org.smooth.systems.ec.migration.model;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * Created by Smooth Systems Solutions
 */
@Data
public class ProductTierPriceStrategy {

  public enum DiscountType {
    PRICE, PERCENTAGE
  }

  private Long id;

  private Long minQuantity;

  private LocalDateTime from;

  private LocalDateTime to;

  private Double newPrice;

  private DiscountType discountType;

  private boolean discountTaxIncluded;
}
