package org.smooth.systems.ec.utils.migration.action;

import lombok.extern.slf4j.Slf4j;
import org.smooth.systems.ec.client.api.MigrationSystemReader;
import org.smooth.systems.ec.client.api.MigrationSystemWriter;
import org.smooth.systems.ec.client.util.ObjectIdMapper;
import org.smooth.systems.ec.component.MigrationSystemReaderAndWriterFactory;
import org.smooth.systems.ec.configuration.MigrationConfiguration;
import org.smooth.systems.ec.utils.db.api.IActionExecuter;
import org.smooth.systems.ec.utils.migration.component.ProductsCache;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public abstract class AbstractProductsMigrationExecuter implements IActionExecuter {

	@Autowired
	protected MigrationConfiguration config;

	@Autowired
	protected MigrationSystemReaderAndWriterFactory readerWriterFactory;

	/**
	 * Maps the product id from the alternative language to the product id of the
	 * main language id (only source system)
	 *
	 * <ul>
	 * <li>key ... product id german language source system</li>
	 * <li>value ... main product id source system (italian language)</li>
	 * </ul>
	 */
	protected ObjectIdMapper productIdsSourceSystem;

	/**
	 * Maps the product id from the source system to the product id of the
	 * uploaded product
	 *
	 * <ul>
	 * <li>key ... product id source system</li>
	 * <li>value ... product id destination system</li>
	 * </ul>
	 */
	protected ObjectIdMapper productIdsMigration;

	protected ProductsCache productsCache;

	protected void initialize() {
		log.info("Read product merging mapping from: {}", config.getGeneratedProductsMergingFile());
		productIdsSourceSystem = new ObjectIdMapper(config.getGeneratedProductsMergingFile());
		productIdsSourceSystem.initializeIdMapperFromFile();
		productIdsMigration = new ObjectIdMapper(config.getGeneratedCreatedProductsMigrationFile());

		MigrationSystemReader reader = readerWriterFactory.getMigrationReader();
		MigrationSystemWriter writer = readerWriterFactory.getMigrationWriter();
	}
}
