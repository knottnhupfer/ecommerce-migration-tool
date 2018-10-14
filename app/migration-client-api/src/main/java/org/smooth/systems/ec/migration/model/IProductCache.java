package org.smooth.systems.ec.migration.model;

public interface IProductCache {

	Product getProductBySku(String sku);

	Product getProductById(Long productId);

	boolean existsProductWithSku(String sku);

	boolean existsProductWithId(Long productId);
}
