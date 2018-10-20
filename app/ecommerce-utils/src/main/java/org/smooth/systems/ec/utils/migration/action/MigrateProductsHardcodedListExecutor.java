package org.smooth.systems.ec.utils.migration.action;

import lombok.extern.slf4j.Slf4j;
import org.smooth.systems.ec.utils.EcommerceUtilsActions;
import org.smooth.systems.ec.utils.migration.model.MigrationProductData;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions>
 */
@Slf4j
@Component
public class MigrateProductsHardcodedListExecutor extends MigrateProductsExecutor {

	@Override
	public String getActionName() {
		return EcommerceUtilsActions.PRODUCTS_MIGRATE_FROM_LIST;
	}

	@Override
	protected List<MigrationProductData> generateProductsList() {
		return ProductMigrationHardcodedListHelper.generateProductsList();
	}
}
