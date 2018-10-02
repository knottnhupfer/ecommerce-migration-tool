package org.smooth.systems.ec.utils.migration.action;

import org.smooth.systems.ec.utils.migration.model.MigrationProductData;
import org.smooth.systems.ec.utils.migration.model.MigrationProductDataBuilder;

import java.util.List;

public final class ProductMigrationHardcodedListHelper {

	private ProductMigrationHardcodedListHelper() {
	}

	public static List<MigrationProductData> generateProductsList() {
		MigrationProductDataBuilder builder = new MigrationProductDataBuilder();
		builder.addMultilingualProduct(3610L, 3608L);
		builder.addMultilingualProduct(3596L, 3597L);
		builder.addMultilingualProduct(3324L, 3330L);
		builder.addMultilingualProduct(2430L, 3372L);
		builder.addMultilingualProduct(3354L, 2630L);
		builder.addMultilingualProduct(2473L, 2659L);
		return builder.getMigratedProducts();
	}
}
