package org.smooth.systems.ec.migration.model;

import lombok.Data;

/**
 * Created by Smooth Systems Solutions
 */
@Data
public class ProductDimensionAndShipping {

  private Double weight;

  private Double width;

  private Double height;

  private Double depth;

  private Double additionalShippingCosts;
}
