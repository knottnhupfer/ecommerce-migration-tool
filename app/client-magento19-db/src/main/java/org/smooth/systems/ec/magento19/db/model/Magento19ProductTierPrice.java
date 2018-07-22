package org.smooth.systems.ec.magento19.db.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name="catalog_product_entity_tier_price")
public class Magento19ProductTierPrice {

  @Id
  @Column(name = "value_id")
  private Long id;

  @Column(name = "entity_id")
  private Long productId;

  @Column(name = "qty")
  private Double quantity;

  @Column(name = "value")
  private Double grossPrice;
}