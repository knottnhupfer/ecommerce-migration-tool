package org.smooth.systems.ec.utils.migration.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.smooth.systems.ec.client.api.ProductId;
import org.springframework.util.Assert;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions> on
 * 29.05.18.
 */
@Data
public class MigrationProductData {

  private ProductId mainProduct;

  private List<ProductId> alternativeProducts = new ArrayList<>();

  public MigrationProductData(ProductId... products) {
    Assert.isTrue(products.length >= 1, "there must be at least one product");
		List<ProductId> productsList = new ArrayList<>(Arrays.asList(products));
    mainProduct = productsList.remove(0);
    alternativeProducts = productsList;
  }

  public List<ProductId> getAsFullList() {
		List<ProductId> res = new ArrayList<>();
		res.add(mainProduct);
    res.addAll(alternativeProducts);
    return res;
  }
}