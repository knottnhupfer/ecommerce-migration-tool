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
@Table(name="catalog_category_entity_text")
public class Magento19CategoryText extends Magento19Attributes {

	public static final Long DESCRIPTION_ATTR_ID = 34L;
	public static final Long META_TAGS_ATTR_ID = 37L;
	public static final Long META_DESCRIPTION_ATTR_ID = 38L;
}