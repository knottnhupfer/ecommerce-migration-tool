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

  /**
   * Depending on the discountType this can be either the reduced price or the discount percentage
   */
  private Double value;

  private DiscountType discountType;

  private boolean discountTaxIncluded;
}
