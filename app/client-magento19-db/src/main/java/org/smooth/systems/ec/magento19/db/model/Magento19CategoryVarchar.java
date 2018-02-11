package org.smooth.systems.ec.magento19.db.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions> on 09.02.18.
 *
 * attribute_id ... 31 == name
 * attribute_id ... 33 == friendly-url
 * attribute_id ... 36 == sub title
 * attribute_id ... 47 == full url store dependent, storeId == 0
 */
@Entity
@Table(name="catalog_category_entity_varchar")
public class Magento19CategoryVarchar extends Magento19Attributes {

	public static final Long NAME_ATTR_ID = 31L;
	public static final Long FRIENDLY_URL_ATTR_ID = 33L;
	public static final Long SUB_TITLE_ATTR_ID = 36L;
	public static final Long FULL_URL_ATTR_ID = 47L; // not used probably but nice for checks later on
}