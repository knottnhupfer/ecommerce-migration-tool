package org.smooth.systems.ec.migration.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProductPriceStrategies {

  private Long productId;

  private boolean hasPriceStrategies;

  private Double netPrice;

  private List<ProductTierPriceStrategy> priceStrategies = new ArrayList<>();

  public void addTierPriceStrategy(ProductTierPriceStrategy strategy) {
    priceStrategies.add(strategy);
  }
}
