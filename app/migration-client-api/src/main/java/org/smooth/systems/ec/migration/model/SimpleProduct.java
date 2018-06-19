package org.smooth.systems.ec.migration.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SimpleProduct {

  private Long id;

  private String sku;

  private String typeId;

  private boolean activated;
}
