package org.smooth.systems.ec.magento19.db.model;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions> on 09.02.18.
 *
 * attribute_id ... 34 == short description
 * attribute_id ... 37 == META tags
 * attribute_id ... 38 == description
 */
@Entity
@Table(name="catalog_product_entity_text")
public class Magento19ProductText extends Magento19Attributes {

	public static final Long DESCRIPTION_ATTR_ID = 57L;
	public static final Long SHORT_DESCRIPTION_ATTR_ID = 58L;
	public static final Long META_TAGS_ATTR_ID = 68L;
}