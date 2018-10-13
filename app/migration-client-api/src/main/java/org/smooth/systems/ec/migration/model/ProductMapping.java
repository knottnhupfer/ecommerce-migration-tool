package org.smooth.systems.ec.migration.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductMapping {

	private String sku;

	private Long srcProductId;

	private Long dstProductId;

	public ProductMapping(String sku, Long srcProductId) {
		this.sku = sku;
		this.srcProductId = srcProductId;
	}
}
