package org.smooth.systems.ec.magento19.db.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name="catalog_product_entity")
public class Magento19Product {

  @Id
  @Column(name = "entity_id")
  private Long id;

  private String sku;

	@Column(name = "type_id")
  private String typeId;
}