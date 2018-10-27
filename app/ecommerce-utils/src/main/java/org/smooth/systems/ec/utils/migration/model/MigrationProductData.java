package org.smooth.systems.ec.utils.migration.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.smooth.systems.ec.client.api.ObjectId;
import org.springframework.util.Assert;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions> on
 * 29.05.18.
 */
@Data
public class MigrationProductData {

  private ObjectId mainProduct;

  private List<ObjectId> alternativeProducts = new ArrayList<>();

  public MigrationProductData(ObjectId... products) {
    Assert.isTrue(products.length >= 1, "there must be at least one product");
		List<ObjectId> productsList = new ArrayList<>(Arrays.asList(products));
    mainProduct = productsList.remove(0);
    alternativeProducts = productsList;
  }

  public List<ObjectId> getAsFullList() {
		List<ObjectId> res = new ArrayList<>();
		res.add(mainProduct);
    res.addAll(alternativeProducts);
    return res;
  }
}