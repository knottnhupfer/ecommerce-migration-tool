package org.smooth.systems.ec.magento19.db.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions> on 09.02.18.
 */
@Data
@Entity
@Table(name = "catalog_product_index_eav")
public class Magento19ProductIndexEav {

	@Id
	@Column(name = "entity_id")
	private Long productId;

	private Long attributeId;

	private String value;
}