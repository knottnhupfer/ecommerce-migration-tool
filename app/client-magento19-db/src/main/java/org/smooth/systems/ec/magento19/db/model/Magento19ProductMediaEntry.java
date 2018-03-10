package org.smooth.systems.ec.magento19.db.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions> on 09.02.18.
 *
 * attribute_id ... 34 == short description
 * attribute_id ... 37 == META tags
 * attribute_id ... 38 == description
 */
@Data
@Entity
@Table(name="catalog_product_entity_media_gallery")
public class Magento19ProductMediaEntry {

  @Id
  @Column(name = "value_id")
  private Long id;

  private Long entityId;

  private Long attributeId;

  private String value;
}