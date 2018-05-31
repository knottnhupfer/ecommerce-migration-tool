package org.smooth.systems.ec.utils.migration.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.smooth.systems.ec.client.api.SimpleProduct;
import org.springframework.util.Assert;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions> on
 * 29.05.18.
 */
@Data
public class MigrationProductData {

  private SimpleProduct mainProduct;

  private List<SimpleProduct> alternativeProducts = new ArrayList<>();

  public MigrationProductData(SimpleProduct... products) {
    Assert.isTrue(products.length >= 1, "there must be at least one product");
    List<SimpleProduct> productsList = Arrays.asList(products);
    mainProduct = productsList.remove(0);
    alternativeProducts = productsList;
  }

  public List<SimpleProduct> getAsFullList() {
    List<SimpleProduct> res = Arrays.asList(mainProduct);
    res.addAll(alternativeProducts);
    return res;
  }
}
