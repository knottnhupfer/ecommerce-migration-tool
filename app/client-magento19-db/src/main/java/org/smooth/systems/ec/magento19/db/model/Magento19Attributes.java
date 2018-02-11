package org.smooth.systems.ec.magento19.db.model;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions> on 09.02.18.
 */
@Data
@MappedSuperclass
public class Magento19Attributes {

	@Id
	@Column(name = "value_id")
	private Long id;

	private Long storeId;

	private Long entityId;

	private Long attributeId;

	private String value;
}