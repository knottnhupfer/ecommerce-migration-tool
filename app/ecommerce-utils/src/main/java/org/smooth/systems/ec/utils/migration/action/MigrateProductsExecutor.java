package org.smooth.systems.ec.utils.migration.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.smooth.systems.ec.client.api.SimpleProduct;
import org.smooth.systems.ec.client.util.ObjectIdMapper;
import org.smooth.systems.ec.component.MigrationSystemReaderAndWriterFactory;
import org.smooth.systems.ec.configuration.MigrationConfiguration;
import org.smooth.systems.ec.exceptions.NotFoundException;
import org.smooth.systems.ec.exceptions.NotImplementedException;
import org.smooth.systems.ec.migration.model.Product;
import org.smooth.systems.ec.utils.db.api.IActionExecuter;
import org.smooth.systems.ec.utils.db.component.AbstractProductsForCategoryReader;
import org.smooth.systems.ec.utils.migration.component.ProductsCache;
import org.smooth.systems.ec.utils.migration.model.MigrationProductData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions> on
 * 29.05.18.
 */
@Slf4j
@Component
public class MigrateProductsExecutor extends AbstractProductsForCategoryReader implements IActionExecuter {

  @Autowired
  private MigrationConfiguration config;

  @Autowired
  private MigrationSystemReaderAndWriterFactory readerWriterFactory;

  private ProductsCache productsCache;

  /**
   * Maps the product id from the alternative language to the product id of the
   * main language id (only source system)
   * 
   * <ul>
   * <li>key ... product id german language source system</li>
   * <li>value ... main product id source system (italian language)</li>
   * </ul>
   */
  private ObjectIdMapper productIdsSourceSystem;

  /**
   * Maps the product id from the source system to the product id of the
   * uploaded product
   * 
   * <ul>
   * <li>key ... product id source system</li>
   * <li>value ... product id</li>
   * </ul>
   * destination system
   */
  private ObjectIdMapper productIdsMigration;

  @PostConstruct
  public void init() {
    log.info("Read product merging mapping from: {}", config.getGeneratedProductsMergingFile());
    productIdsSourceSystem = new ObjectIdMapper(config.getGeneratedProductsMergingFile());
  }

  @Override
  public String getActionName() {
    return "products-migrate";
  }

  @Override
  public void execute() {
    log.trace("execute()");

    List<MigrationProductData> productList = generateProductsList("it", "de");
    log.info("Generated basic product data for migration successfully");

    initializeProductsCache(productList);
    log.info("Products cache initialized successfully");

    List<Product> mergedProducts = mergeProducts(productList);
    log.info("Successfully merged products");

    List<Product> filledUpProducts = fillUpProductsWithMissingLanguage(mergedProducts);
    log.info("Successfully filled up products");

    uploadProductsAndUpdateMapping(filledUpProducts);
    log.info("Successfully migrated all products");
  }

  private void initializeProductsCache(List<MigrationProductData> productList) {
    List<SimpleProduct> collect = productList.stream().map(data -> data.getAsFullList()).collect(Collectors.toList()).stream()
        .flatMap(x -> x.stream()).collect(Collectors.toList());
    productsCache = ProductsCache.buildProductsCache(readerWriterFactory.getMigrationReader(), collect);
  }

  private List<MigrationProductData> generateProductsList(String mainLangCode, String alternativeLangCode) {
    List<MigrationProductData> products = new ArrayList<>();

    Set<Long> alternativeProductIds = productIdsSourceSystem.keySet();
    try {
      for (Long prodId : alternativeProductIds) {
        SimpleProduct mainProduct = SimpleProduct.builder().productId(productIdsSourceSystem.getMappedIdForId(prodId)).langIso(mainLangCode)
            .build();
        SimpleProduct altProduct = SimpleProduct.builder().productId(prodId).langIso(alternativeLangCode).build();
        products.add(new MigrationProductData(mainProduct, altProduct));
      }
    } catch (NotFoundException e) {
      log.error("Error while preparing data, actually can't happen!");
      throw new IllegalStateException("Error while preparing data, actually can't happen!");
    }
    return products;
  }

  private List<Product> mergeProducts(List<MigrationProductData> productList) {
    throw new NotImplementedException();
  }

  private List<Product> fillUpProductsWithMissingLanguage(List<Product> productList) {
    // TODO: check if all products are filled with 2 languages
    // TODO: what to do with only Italian products, not in list
    // TODO: what to do with only German products, not in list and filtered earlier
    throw new NotImplementedException();
  }

  private void uploadProductsAndUpdateMapping(List<Product> filledUpProducts) {
    throw new NotImplementedException();
  }
}