package org.smooth.systems.ec.utils.db.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="catalog_category_entity")
public class MagentoCategory {

  @Id
  @Column(name = "entity_id")
  private Long id;

  private Long level;
}