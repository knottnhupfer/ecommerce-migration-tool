package org.smooth.systems.ec.utils.db.component;

import java.util.List;
import java.util.Properties;

import org.smooth.systems.ec.utils.db.model.MagentoCategory;
import org.smooth.systems.ec.utils.db.repository.CategoriesRepository;
import org.smooth.systems.ec.utils.db.repository.ProductCategoryMappingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
//import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ProductsToCategoryMappingGenerator implements ApplicationRunner {

  @Autowired
  private CategoriesRepository categoriesRepo;

  @Autowired
  private ProductCategoryMappingRepository productsCategoryRepo;

  @Override
  public void run(ApplicationArguments applicationArguments) throws Exception {
    log.trace("run()");
    Properties properties = retrieveAllProductsWithCategoryMapping();
    log.info("Properties({})", properties.size());
    log.info("{}", properties);
  }

  private Properties retrieveAllProductsWithCategoryMapping() {
    log.info("retrieveAllProductsAndCategoryMapping()");
    List<Long> allProductIds = productsCategoryRepo.findAllProductIds();
    log.info("Found {} products", allProductIds.size());

    Properties properties = new Properties();
    for (Long productId : allProductIds) {
      properties.put(productId, getCategoryIdForProductId(productId));
    }
    return properties;
  }

  private Long getCategoryIdForProductId(Long productId) {
    log.debug("getCategoryIdForProductId({})", productId);
    List<Long> categoryIds = productsCategoryRepo.findByProductId(productId);
    if (categoryIds.isEmpty()) {
      String msg = String.format("No categoryIds found for productId:%s", productId);
      log.error(msg);
      throw new RuntimeException(msg);
    }
    log.trace("Found {} categories for productId {}", categoryIds.size(), productId);

    List<MagentoCategory> categories = categoriesRepo.getByCategoryIds(categoryIds);
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