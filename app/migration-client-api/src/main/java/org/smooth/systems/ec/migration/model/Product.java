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

  private Double netPrice;

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
    return String.format("Product(id=%d, sku=%s, brandId=%d, price=%.2f, categories=%s, activated=%b, productImageUrls=%s)",
            id, sku, brandId, netPrice, categories, activated, productImageUrls);
  }
}