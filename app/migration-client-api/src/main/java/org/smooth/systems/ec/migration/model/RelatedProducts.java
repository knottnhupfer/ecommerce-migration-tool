package org.smooth.systems.ec.migration.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class RelatedProducts {

	private ProductMapping product;

	private List<ProductMapping> relatedProducts = new ArrayList<>();

	public RelatedProducts(String sku, Long srcProductId) {
		product = new ProductMapping(sku, srcProductId);
	}

	public void addRelatedProduct(ProductMapping relatedProduct) {
		relatedProducts.add(relatedProduct);
	}
}
