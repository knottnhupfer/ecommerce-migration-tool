package org.smooth.systems.ec.utils.migration.action;

import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.smooth.systems.ec.client.api.MigrationSystemReader;
import org.smooth.systems.ec.client.api.ProductId;
import org.smooth.systems.ec.client.util.ObjectIdMapper;
import org.smooth.systems.ec.component.MigrationSystemReaderAndWriterFactory;
import org.smooth.systems.ec.configuration.MigrationConfiguration;
import org.smooth.systems.ec.exceptions.NotFoundException;
import org.smooth.systems.ec.migration.model.IProductCache;
import org.smooth.systems.ec.migration.model.IProductMetaData;
import org.smooth.systems.ec.client.api.MigrationSystemWriter;
import org.smooth.systems.ec.utils.db.api.IActionExecuter;
import org.smooth.systems.ec.utils.migration.component.ProductsCache;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions>
 */
@Slf4j
public abstract class AbstractProductsMigrationExecuter implements IActionExecuter {

  @Autowired
  protected MigrationConfiguration config;

  @Autowired
  protected MigrationSystemReaderAndWriterFactory readerWriterFactory;

  private String actionName;

  /**
   * Maps the product id from the alternative language to the product id of the
   * main language id (only source system)
   *
   * <ul>
   * <li>key ... product id german language source system</li>
   * <li>value ... main product id source system (italian language)</li>
   * <li>value ... main product id source system (italian language)</li>
   * </ul>
   */
  protected ObjectIdMapper productIdsSourceSystem;

  protected MigrationSystemReader readerSrcSystem;
  protected MigrationSystemWriter writerDstSystem;

  protected IProductCache srcProductsCache;
	protected IProductCache dstProductsCache;

	protected HashMap<String, IProductMetaData> existingProductsDestinationSystem;

	public AbstractProductsMigrationExecuter(String actionName) {
		this.actionName = actionName;
	}

	@Override
	public String getActionName() {
		return actionName;
	}

	protected void initialize() {
    log.info("Read product merging mapping from: {}", config.getGeneratedProductsMergingFile());
    productIdsSourceSystem = new ObjectIdMapper(config.getGeneratedProductsMergingFile());
    productIdsSourceSystem.initializeIdMapperFromFile();

		initializeReaderAndWriter();
		populateDestinationSystemMetaData();
  }

	protected void initializeReaderAndWriter() {
		readerSrcSystem = readerWriterFactory.getMigrationReader();
		writerDstSystem = readerWriterFactory.getMigrationWriter();
	}

	protected void logExecutionStep(Logger logger, String msg, Object... params) {
		logger.info("EXECUTION: " + msg, params);
	}

	protected boolean doesProductOnDestinationSystemWithSkuExists(String sku) {
		return existingProductsDestinationSystem.containsKey(sku);
	}

	protected Long getProductIdDestinationSystemForProductSku(String sku) {
		if(!doesProductOnDestinationSystemWithSkuExists(sku)) {
			throw new RuntimeException(String.format("Unable to retrieve productId for product sku: '{}'", sku));
		}
    return existingProductsDestinationSystem.get(sku).getProductId();
	}

	protected void initializeEmptyDestinationSystemProductCache() {
		if(dstProductsCache != null) {
			throw new IllegalStateException("Destination product cache already initialized.");
		}
		log.info("Initialize destination product cache");
		dstProductsCache = ProductsCache.createProductsCache(getReaderForDestinationSystem());
	}

	protected void populateDestinationSystemMetaData() {
		log.info("Read existing products from destination system ...");
		existingProductsDestinationSystem = new HashMap<>();
		MigrationSystemReader reader = getReaderForDestinationSystem();
		List<IProductMetaData> productMetaData = reader.readAllProductsMetaData();
		productMetaData.forEach(prod -> existingProductsDestinationSystem.put(prod.getSku(), prod));
		log.info("Retrieved and cached {} products meta data", productMetaData.size());
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
    log.info("Retrieved {} main product ids for processing.", mainProductIds.size());
    return mainProductIds;
  }

	protected void initializeEmptySourceSystemProductCache() {
  	if(srcProductsCache != null) {
  		throw new IllegalStateException("Product cache already initialized.");
		}
		log.info("Initialize source product cache");
		srcProductsCache = ProductsCache.createProductsCache(readerWriterFactory.getMigrationReader());
	}

  protected List<ProductId> initializeSourceSystemProductCacheAndRetrieveList() {
		if(srcProductsCache != null) {
			throw new IllegalStateException("Product cache already initialized.");
		}
    List<ProductId> mainProductIds = retrieveMainProductIds();
    srcProductsCache = ProductsCache.createProductsCache(readerWriterFactory.getMigrationReader(), mainProductIds);
    return mainProductIds;
  }

	protected IProductMetaData getProductsMetaDataFromDestinationSystemForSku(String sku) {
  	if(!doesProductOnDestinationSystemWithSkuExists(sku)) {
			String msg = String.format("No product exists on destination system with sku '{}'", sku);
			log.error(msg);
			throw new IllegalStateException(msg);
		}
		return existingProductsDestinationSystem.get(sku);
	}

  protected List<IProductMetaData> getProductsMetaDataFromDestinationSystem() {
		return Collections.unmodifiableList(new ArrayList<>(existingProductsDestinationSystem.values()));
	}

	protected MigrationSystemReader getReaderForDestinationSystem() {
		try {
			return readerWriterFactory.getSystemReaderForType(config.getDestinationSystemName());
		} catch (NotFoundException e) {
			String msg = String.format("Destination system '%s' does not provide any reader.", config.getDestinationSystemName());
			log.error(msg);
			throw new RuntimeException(msg);
		}
	}
}
