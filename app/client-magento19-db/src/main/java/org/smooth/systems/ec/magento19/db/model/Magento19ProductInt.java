package org.smooth.systems.ec.magento19.db.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "catalog_product_entity_int")
public class Magento19ProductInt {

  @Id
  @Column(name = "value_id")
  private Long id;

  @Column(name = "entity_id")
  private Long productId;

  private Long attributeId;

  private Integer value;
}