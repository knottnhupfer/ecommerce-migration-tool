package org.smooth.systems.ec.utils.db.component;

import lombok.extern.slf4j.Slf4j;
import org.smooth.systems.ec.configuration.MigrationConfiguration;
import org.smooth.systems.ec.magento19.db.repository.ProductCategoryMappingRepository;
import org.smooth.systems.ec.utils.db.api.IActionExecuter;
import org.smooth.systems.ec.utils.db.model.MagentoCategory;
import org.smooth.systems.ec.utils.db.repository.CategoriesRepository;
import org.smooth.systems.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Properties;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions> on 03.02.18.
 */
@Slf4j
//@Component
public class ProductsToCategoryMappingExecutor implements IActionExecuter {

  @Autowired
  private CategoriesRepository categoriesRepo;

  @Autowired
  private ProductCategoryMappingRepository productsCategoryRepo;

  @Autowired
  private MigrationConfiguration config;

  @Override
  public String getActionName() {
    return "products-mapping";
  }

  @Override
  public void execute() {
    log.trace("execute()");
    Properties properties = retrieveAllProductsWithCategoryMapping();
    log.info("Properties({})", properties.size());
    log.info("{}", properties);
    FileUtils.writePropertiesToFile(properties, config.getGeneratedCreatedCategoriesMappingFile(), " mapping from productId to categoryId\n# productId=categoryId");
  }

  private Properties retrieveAllProductsWithCategoryMapping() {
    log.info("retrieveAllProductsAndCategoryMapping()");
    throw new RuntimeException("Not used anymore");

//		List<Long> allProductIds = productsCategoryRepo.findAllProductIds();
//		log.info("Found {} products", allProductIds.size());
//
//		Properties properties = new Properties();
//		for (Long productId : allProductIds) {
//			properties.put(productId.toString(), getCategoryIdForProductId(productId).toString());
//		}
//		return properties;
  }

  private Long getCategoryIdForProductId(Long productId) {
    log.debug("getCategoryIdForProductId({})", productId);
    List<Long> categoryIds = productsCategoryRepo.getCategoryIdsforProductId(productId);
    if (categoryIds.isEmpty()) {
      String msg = String.format("No categoryIds found for productId:%s", productId);
      log.error(msg);
      throw new RuntimeException(msg);
    }
    log.trace("Found {} categories for productId {}", categoryIds.size(), productId);

    List<MagentoCategory> categories = categoriesRepo.getByCategoryIdsOrderedByLevel(categoryIds);
    if (categories.isEmpty()) {
      String msg = String.format("No categories found for productId:%s", productId);
      log.error(msg);
      throw new RuntimeException(msg);
    }
    if (categoryIds.size() != categories.size()) {
      log.warn("Unable to fetch proper category size({}), found categories: {}", categoryIds.size(), categories);
    }
    log.trace("Found {} categories for productId {}", categoryIds.size(), productId);

    return categories.get(0).getId();
  }
}
