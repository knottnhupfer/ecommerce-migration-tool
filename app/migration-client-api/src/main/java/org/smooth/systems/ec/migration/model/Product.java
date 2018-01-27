package org.smooth.systems.ec.migration.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * Created by Smooth Systems Solutions
 */
@Data
public class Product {

  public enum ProductType {
    Simple, Configurable
  }

  public enum ProductVisibility {
    Everywhere, Catalog, Search, NotVisible
  }

  private Long id;

  private String sku;

  private LocalDateTime creationDate;

  private long taxRuleId;

  private Double salesPrice;

  private Double costPrice;

  private ProductType type;

  private ProductVisibility visibility;

  private Long brandId;

  private ProductDimensionAndShipping dimension;

  private ProductTranslateableAttributes attributes;

  private List<Long> categories = new ArrayList<>();

  private List<Long> relatedProducts = new ArrayList<>();

  private List<String> productImageUrls = new ArrayList<>();
}
