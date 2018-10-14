package org.smooth.systems.ec.magento19.db.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Magento19RelatedProduct {

	@Id
	@Column(name = "link_id")
	private Long linkId;

  @Column(name = "product_id")
  private Long productId;

  @Column(name = "linked_product_id")
  private Long relatedProductId;
}