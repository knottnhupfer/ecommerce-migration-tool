package org.smooth.systems.ec.migration.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Smooth Systems Solutions
 */
@Data
@NoArgsConstructor
public class Product {

  public enum ProductType {
    Simple
  }

  public enum ProductVisibility {
    Everywhere, Catalog, Search, NotVisible
  }

  private Long id;

  private String sku;

  private Boolean activated;

  private LocalDateTime creationDate;

  // private long taxRuleId;

  private Double salesPrice;

  private Double costPrice;

  private ProductType type;

  private ProductVisibility visibility;

  private Long brandId;

  private ProductDimensionAndShipping dimension;

  private List<ProductTranslateableAttributes> attributes = new ArrayList<>();

  private List<Long> categories = new ArrayList<>();

  private List<Long> relatedProducts = new ArrayList<>();

  private List<String> productImageUrls = new ArrayList<>();

  public Product(Long id, String sku, LocalDateTime creationDate) {
    this.id = id;
    this.sku = sku;
    this.type = ProductType.Simple;
    this.creationDate = creationDate;
  }

  public String simpleDescription() {
    return String.format("\nProduct(id=%d, sku=%s, brandId=%d, categories=%s, productImageUrls=%s, activated=%b)", id, sku, brandId, categories, productImageUrls, activated);
  }
}