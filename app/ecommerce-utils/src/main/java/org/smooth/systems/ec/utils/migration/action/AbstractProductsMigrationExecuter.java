package org.smooth.systems.ec.utils.migration.action;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.smooth.systems.ec.client.api.MigrationSystemReader;
import org.smooth.systems.ec.client.util.ObjectIdMapper;
import org.smooth.systems.ec.component.MigrationSystemReaderAndWriterFactory;
import org.smooth.systems.ec.configuration.MigrationConfiguration;
import org.smooth.systems.ec.exceptions.NotFoundException;
import org.smooth.systems.ec.migration.model.IProductMetaData;
import org.smooth.systems.ec.utils.migration.component.ProductsCache;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;

@Slf4j
public abstract class AbstractProductsMigrationExecuter {

	@Autowired
	protected MigrationConfiguration config;

	@Autowired
	private MigrationSystemReaderAndWriterFactory readerWriterFactory;

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

	protected ProductsCache productsCache;

	private HashMap<String, IProductMetaData> existingProductsDestinationSystem;

	protected void initialize() {
		log.info("Read product merging mapping from: {}", config.getGeneratedProductsMergingFile());
		productIdsSourceSystem = new ObjectIdMapper(config.getGeneratedProductsMergingFile());
		productIdsSourceSystem.initializeIdMapperFromFile();
		populateDestinationSystemProductCache();
	}

	protected void logExecutionStep(Logger logger, String msg, Object... params) {
		logger.info("EXECUTION: " + msg, params);
	}

	protected boolean doesProductWithSkuExists(String sku) {
		return existingProductsDestinationSystem.containsKey(sku);
	}

	protected Long getProductIdDestinationSystemForProductSku(String sku) {
		if(!doesProductWithSkuExists(sku)) {
			throw new RuntimeException(String.format("Unable to retrieve productId for product sku: '{}'", sku));
		}
		return existingProductsDestinationSystem.get(sku).getProductId();
	}

	private void populateDestinationSystemProductCache() {
		try {
			MigrationSystemReader reader = readerWriterFactory.getSystemReaderForType(config.getDestinationSystemName());
			List<IProductMetaData> productMetaData = reader.readAllProductsMetaData();
			productMetaData.forEach(prod -> existingProductsDestinationSystem.put(prod.getSku(), prod));
			log.info("Retrieved and cached {} products", productMetaData.size());
		} catch (NotFoundException e) {
			String msg = String.format("Destination system '{}' does not provide any reader.");
			log.error(msg);
			throw new RuntimeException(msg);
		}
	}
}
