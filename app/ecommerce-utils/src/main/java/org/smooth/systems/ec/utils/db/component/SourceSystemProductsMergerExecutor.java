package org.smooth.systems.ec.utils.db.component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.smooth.systems.ec.client.api.SimpleCategory;
import org.smooth.systems.ec.client.util.ObjectIdMapper;
import org.smooth.systems.ec.configuration.MigrationConfiguration;
import org.smooth.systems.ec.migration.model.SimpleProduct;
import org.smooth.systems.ec.utils.EcommerceUtilsActions;
import org.smooth.systems.ec.utils.db.api.IActionExecuter;
import org.smooth.systems.ec.utils.db.model.MagentoProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions> on 03.02.18.
 */
@Slf4j
@Component
public class SourceSystemProductsMergerExecutor extends AbstractProductsForCategoryReader implements IActionExecuter {

  @Autowired
  private MigrationConfiguration config;

  private Map<String, SimpleProduct> productMap = new HashMap<>();

  private ObjectIdMapper productIdsMapper;

  @Override
  public String getActionName() {
    return EcommerceUtilsActions.PRODUCTS_MAPPING;
  }

  @Override
  public void execute() {
    log.trace("execute()");
    initializeRootCategories();
    log.info("Mapping will be written to file: {}", config.getGeneratedProductsMergingFile());

    for (SimpleCategory categoryConfig : config.getAdditionalCategories()) {
      log.info("Merge category: {}", categoryConfig);
      mergeProductIdsToRootProducts(categoryConfig);
    }
    productIdsMapper.writeMappingToFile(" mapping additional language productId to rootProductId (both ids are present in the source system)");
  }

  private void initializeRootCategories() {
    List<SimpleProduct> rootProducts = getActivatedCategoryProducts(config.getRootCategoryId());
    log.info("Retrieved root products: {}", rootProducts);
    filterProductsWithProductsToBeIgnoredAndValidateProducts("Main products", rootProducts);
    productIdsMapper = new ObjectIdMapper(config.getGeneratedProductsMergingFile());
    rootProducts.forEach(product -> productMap.put(product.getSku(), product));
  }

  private void mergeProductIdsToRootProducts(SimpleCategory categoryConfig) {
    log.info("mergeProductIdsToRootProducts({})", categoryConfig);
    List<SimpleProduct> products = getActivatedCategoryProducts(categoryConfig.getCategoryId());
    filterProductsWithProductsToBeIgnoredAndValidateProducts(String.format("Products for category: %s", categoryConfig), products);
    log.info("{} products to be merged", products.size());

    products.forEach(product -> {
      String productSku = beautifyProductSku(product);
      if (productMap.keySet().contains(productSku)) {
        SimpleProduct matchingRootProduct = productMap.get(productSku);

        // Assert.isTrue(!product.getId().equals(matchingRootProduct.getId()), String.format("Product can't be mapped to itself. Product: %s" , product));
        if(!product.getId().equals(matchingRootProduct.getId())) {
          productIdsMapper.addMapping(product.getId(), matchingRootProduct.getId());
        } else {
          // TODO write product ids list to file where activated is false
          log.error("Product can't be mapped to itself. Product: {}" , product);
        }
      }
    });
    products.removeIf(product -> productIdsMapper.keySet().contains(product.getId()));
    log.info("{} not merged products", products.size());
    if (!products.isEmpty()) {
      log.error("Not merged products: {}", products);
      // FIXME: F101, check code in trello
      // TODO generate additional product list for not merged products so that an additional behaviour can be implemented
      // throw new RuntimeException(String.format("Unable to merge %s products.", products.size()));
    }
  }

  private String beautifyProductSku(SimpleProduct product) {
    String sku = product.getSku().trim();
    if (sku.startsWith("_")) {
      sku = sku.substring(1);
    }
    return sku;
  }

  /**
   * Removes products which should be ignored by configuration and validates that the rest of products do have a sku not null and not empty.
   *
   * @param info
   * @param products
   */
  private void filterProductsWithProductsToBeIgnoredAndValidateProducts(String info, List<SimpleProduct> products) {
    log.debug("filterProductsWithProductsToBeIgnoredAndValidateProducts({}, {})", info, products.size());
    removingProductsToBeSkipped(products);

    List<SimpleProduct> invalidProducts = products.stream().filter(product -> product.getSku() == null || product.getSku().isEmpty()).collect(Collectors.toList());
    if (!invalidProducts.isEmpty()) {
      String msg = String.format("%s contains %s invalid products.", info, invalidProducts.size());
      log.error(msg);
      log.error("Invalid products: {}", invalidProducts);
      throw new RuntimeException(msg);
    }
  }

  private void removingProductsToBeSkipped(List<SimpleProduct> products) {
    List<Long> ignoringProductsIds = config.getProductIdsSkipping();
    List<SimpleProduct> productsToBeRemoved = products.stream().filter(product -> ignoringProductsIds.contains(product.getId())).collect(Collectors.toList());
    log.info("Found {} to ignore while migration, {} ids are configured to be ignored.", productsToBeRemoved.size(), ignoringProductsIds.size());
    Assert.isTrue(ignoringProductsIds.size() >= productsToBeRemoved.size(), "check ignoring ids >= foudn products to ignore");

    int startSize = products.size();
    productsToBeRemoved.forEach(product -> {
      products.remove(product);
    });
    int endSize = products.size();
    Assert.isTrue((startSize - endSize) == productsToBeRemoved.size(), "delta size ignoring products does not match");
  }

  private void printoutProductWithEmptySKU(List<MagentoProduct> products, String prefix) {
    products.forEach(product -> {
      if (product.getSku() == null) {
        log.info("{}: id: {}, SKU: {}", prefix, product.getId(), product.getSku());
      }
    });
  }
}
