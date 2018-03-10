package org.smooth.systems.ec.magento19.db.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "catalog_product_entity_decimal")
public class Magento19ProductDecimal {

  @Id
  @Column(name = "value_id")
  private Long id;

  @Column(name = "entity_id")
  private Long productId;

  private Long attributeId;

  private Double value;
}