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
@Table(name = "eav_attribute_option_value")
public class Magento19EavAttributeOption {

	@Id
	@Column(name = "value_id")
	private Long id;

	private Long optionId;

	private String value;
}