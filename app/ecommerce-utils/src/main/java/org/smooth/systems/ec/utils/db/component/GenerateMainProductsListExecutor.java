package org.smooth.systems.ec.utils.db.component;

import lombok.extern.slf4j.Slf4j;
import org.smooth.systems.ec.configuration.MigrationConfiguration;
import org.smooth.systems.ec.utils.db.api.IActionExecuter;
import org.smooth.systems.ec.utils.db.model.MagentoCategory;
import org.smooth.systems.ec.utils.db.repository.CategoriesRepository;
import org.smooth.systems.ec.utils.db.repository.ProductCategoryMappingRepository;
import org.smooth.systems.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Properties;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions> on 03.02.18.
 */
@Slf4j
@Component
public class GenerateMainProductsListExecutor extends AbstractProductsForCategoryReader implements IActionExecuter {

	@Autowired
	private MigrationConfiguration config;

	@Override
	public String getActionName() {
		return "main-products";
	}

	@Override
	public void execute() {
		log.info("execute()");
		List<Long> productIds = retrieveProductIdsForCateoryId(config.getRootCategoryId());
		log.info("Product ids: {}", productIds.size());
		// create mapping for productId to new generated categoryId
	}
}
