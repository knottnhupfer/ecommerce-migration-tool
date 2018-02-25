package org.smooth.systems.ec.magento19.db.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.smooth.systems.ec.migration.model.Product.ProductVisibility;

import lombok.Data;

@Data
@Entity
@Table(name = "catalog_product_enabled_index")
public class Magento19ProductVisibility {

  public static final int VISIBILITY_NOT_VISIBLE = 1;
  public static final int VISIBILITY_IN_CATALOG = 2;
  public static final int VISIBILITY_IN_SEARCH = 3;
  public static final int VISIBILITY_BOTH = 4;

  @Id
  @Column(name = "product_id")
  private Long id;

  @Column(name = "store_id")
  private Long storeId;

  @Column(name = "visibility")
  private Long visibility;

  @Transient
  public ProductVisibility getVisibility() {
    switch (visibility.intValue()) {
    case VISIBILITY_NOT_VISIBLE:
      return ProductVisibility.NotVisible;
    case VISIBILITY_IN_CATALOG:
      return ProductVisibility.Catalog;
    case VISIBILITY_IN_SEARCH:
      return ProductVisibility.Search;
    case VISIBILITY_BOTH:
    default:
      return ProductVisibility.Everywhere;
    }
  }
}