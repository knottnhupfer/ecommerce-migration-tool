package org.smooth.systems.ec.utils.db.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="catalog_category_product")
public class MagentoProductCategoryMapping {

  @Id
  @Column(name = "entity_id")
  private Long id;

  @Column(name = "category_id")
  private Long categoryId;

  @Column(name = "product_id")
  private Long productId;

  private Long position;
}