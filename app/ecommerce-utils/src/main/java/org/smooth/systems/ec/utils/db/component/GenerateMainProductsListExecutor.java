package org.smooth.systems.ec.utils.db.component;

import lombok.extern.slf4j.Slf4j;
import org.smooth.systems.ec.client.util.ObjectIdMapper;
import org.smooth.systems.ec.configuration.MigrationConfiguration;
import org.smooth.systems.ec.utils.db.api.IActionExecuter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

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
		log.debug("execute()");
		List<Long> rootProductIds = retrieveAllProductIdsForCateoryId(config.getRootCategoryId());
		log.info("Retrieved {} product ids for root category id {}", rootProductIds.size(), config.getRootCategoryId());

		ObjectIdMapper productsIdToCategoryMapper = new ObjectIdMapper(config.getGeneratedCreatedCategoriesMappingFile());
		for (Long productId : rootProductIds) {
			productsIdToCategoryMapper.addMapping(productId, getCategoryIdForProductId(productId));
		}
		String comment = " maps root category ids of the source system to category ids on the source system";
		productsIdToCategoryMapper.writeMappingToFile(comment);
	}
}