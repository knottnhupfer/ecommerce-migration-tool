package org.smooth.systems.ec.utils.db.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="catalog_product_entity")
public class MagentoProduct {

  @Id
  @Column(name = "entity_id")
  private Long id;

  private String sku;

  private Long level;
}