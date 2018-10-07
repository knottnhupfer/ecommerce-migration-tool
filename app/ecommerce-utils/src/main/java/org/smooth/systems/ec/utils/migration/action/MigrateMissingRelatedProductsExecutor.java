package org.smooth.systems.ec.utils.migration.action;

import lombok.extern.slf4j.Slf4j;
import org.smooth.systems.ec.migration.model.IProductMetaData;
import org.smooth.systems.ec.utils.EcommerceUtilsActions;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions>
 */
@Slf4j
@Component
public class MigrateMissingRelatedProductsExecutor extends AbstractProductsMigrationExecuter {

	public MigrateMissingRelatedProductsExecutor() {
		super(EcommerceUtilsActions.PRODUCTS_RELATED_PRODUCTS_MISSING);
	}

	@Override
	public void execute() {
		initialize();
		initializeEmptyProductCache();
		log.info("Initialized readers and writer ...");

		// read related products for each product
		// filter all products with no related products
		// http://WUIHXDKPX2WNUUBLAG6BLZ8FIAX6PM6P@prestashop.local/api/
		// LOOP:
		//   - read related products for single product from source system
		//   - upload related products for single product to destination system
	}
}
