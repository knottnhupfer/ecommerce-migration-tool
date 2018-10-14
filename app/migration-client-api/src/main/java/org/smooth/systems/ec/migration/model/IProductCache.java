package org.smooth.systems.ec.migration.model;

public interface IProductCache {

	Product getProductBySku(String sku);

	Product getProductById(Long productId);
}
