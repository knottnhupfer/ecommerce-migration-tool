package org.smooth.systems.ec.utils.migration.action;

import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.smooth.systems.ec.client.api.MigrationSystemReader;
import org.smooth.systems.ec.client.api.ProductId;
import org.smooth.systems.ec.client.util.ObjectIdMapper;
import org.smooth.systems.ec.component.MigrationSystemReaderAndWriterFactory;
import org.smooth.systems.ec.configuration.MigrationConfiguration;
import org.smooth.systems.ec.exceptions.NotFoundException;
import org.smooth.systems.ec.migration.model.IProductMetaData;
import org.smooth.systems.ec.client.api.MigrationSystemWriter;
import org.smooth.systems.ec.utils.db.api.IActionExecuter;
import org.smooth.systems.ec.utils.migration.component.ProductsCache;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions>
 */
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

  protected MigrationSystemReader reader;
  protected MigrationSystemWriter writer;

  protected ProductsCache productsCache;

	private HashMap<String, IProductMetaData> existingProductsDestinationSystem;

  protected void initialize() {
    log.info("Read product merging mapping from: {}", config.getGeneratedProductsMergingFile());
    productIdsSourceSystem = new ObjectIdMapper(config.getGeneratedProductsMergingFile());
    productIdsSourceSystem.initializeIdMapperFromFile();

    reader = readerWriterFactory.getMigrationReader();
    writer = readerWriterFactory.getMigrationWriter();
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
			existingProductsDestinationSystem = new HashMap<>();
			MigrationSystemReader reader = readerWriterFactory.getSystemReaderForType(config.getDestinationSystemName());
			List<IProductMetaData> productMetaData = reader.readAllProductsMetaData();
			productMetaData.forEach(prod -> existingProductsDestinationSystem.put(prod.getSku(), prod));
			log.info("Retrieved and cached {} products meta data", productMetaData.size());
		} catch (NotFoundException e) {
			String msg = String.format("Destination system '{}' does not provide any reader.");
			log.error(msg);
			throw new RuntimeException(msg);
		}
	}

  protected List<ProductId> retrieveMainProductIds() {
    List<ProductId> mainProductIds = new ArrayList<>();
    try {
      for (Long prodId : productIdsSourceSystem.keySet()) {
        Long mainProductId = productIdsSourceSystem.getMappedIdForId(prodId);
        ProductId product = ProductId.builder().productId(mainProductId).langIso(config.getRootCategoryLanguage()).build();
        mainProductIds.add(product);
      }
    } catch (NotFoundException e) {
      throw new IllegalStateException("This exception shouldn't not happen.");
    }
    return mainProductIds;
  }
}
