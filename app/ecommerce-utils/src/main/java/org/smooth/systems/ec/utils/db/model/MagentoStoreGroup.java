package org.smooth.systems.ec.utils.db.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "store_group")
public class MagentoStoreGroup {

  @Id
  @Column(name = "group_id")
  private Long id;

  @Column(name = "root_category_id")
  private Long rootCategoryId;
}