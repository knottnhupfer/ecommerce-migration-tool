package org.smooth.systems.ec.utils.migration.model;

import lombok.Data;
import org.smooth.systems.ec.client.api.ProductId;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProductMetaDataToMigrate {

	private List<MigrationProductData> products = new ArrayList<>();

	private void addProduct(MigrationProductData product) {
		products.add(product);
	}

	public void addMultilingualProduct(Long prodId1, Long prodId2) {
		addProduct(createProduct(prodId1, "it", prodId2, "de"));
	}

	public static MigrationProductData createProduct(Long prodId1, String lang1, Long prodId2, String lang2) {
		ProductId prod1 = ProductId.builder().productId(prodId1).langIso(lang1).build();
		ProductId prod2 = ProductId.builder().productId(prodId2).langIso(lang2).build();
		return new MigrationProductData(prod1, prod2);
	}

	public List<MigrationProductData> getMigratedProducts() {
		return products;
	}
}
