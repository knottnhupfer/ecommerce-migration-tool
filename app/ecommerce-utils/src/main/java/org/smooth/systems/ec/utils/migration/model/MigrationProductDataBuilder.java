package org.smooth.systems.ec.utils.migration.model;

import org.smooth.systems.ec.client.api.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class MigrationProductDataBuilder {

	private List<MigrationProductData> products = new ArrayList<>();

	private void addProduct(MigrationProductData product) {
		products.add(product);
	}

	public void addMultilingualProduct(Long prodId1, Long prodId2) {
		addProduct(createProduct(prodId1, "it", prodId2, "de"));
	}

	public MigrationProductData createProduct(Long prodId1, String lang1, Long prodId2, String lang2) {
		ObjectId prod1 = ObjectId.builder().objectId(prodId1).langIso(lang1).build();
		ObjectId prod2 = ObjectId.builder().objectId(prodId2).langIso(lang2).build();
		return new MigrationProductData(prod1, prod2);
	}

	public List<MigrationProductData> getMigratedProducts() {
		return products;
	}
}
