package org.smooth.systems.ec.utils.migration.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import org.smooth.systems.ec.client.api.SimpleProduct;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions> on
 * 29.05.18.
 */
@Data
public class MigrationProductData {

  private SimpleProduct mainProduct;

  private List<SimpleProduct> alternativeProducts = new ArrayList<>();
}
