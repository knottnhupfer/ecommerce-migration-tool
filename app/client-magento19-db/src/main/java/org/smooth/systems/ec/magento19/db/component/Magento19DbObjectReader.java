package org.smooth.systems.ec.magento19.db.component;

import org.smooth.systems.ec.client.api.SimpleCategory;
import org.smooth.systems.ec.client.api.ProductId;
import org.smooth.systems.ec.client.api.MigrationSystemReader;
import org.smooth.systems.ec.exceptions.NotImplementedException;
import org.smooth.systems.ec.magento19.db.Magento19Constants;
import org.smooth.systems.ec.migration.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions> on
 * 09.02.18.
 */
@Slf4j
@Component
@Profile(Magento19Constants.MAGENTO19_DB_PROFILE_NAME)
public class Magento19DbObjectReader implements MigrationSystemReader {

  @Autowired
  private Magento19DbCategoriesReader categoriesReader;

  @Autowired
  private Magento19DbProductsReader productsReader;

  @Override
  public String getName() {
    return Magento19Constants.MAGENTO19_DB_PROFILE_NAME;
  }

  @Override
  public List<User> readAllUsers() {
    throw new NotImplementedException();
  }

  @Override
  public List<User> readWebUsers() {
    throw new NotImplementedException();
  }

  @Override
  public List<User> readAdministrativeUsers() {
    throw new NotImplementedException();
  }

  @Override
  public List<Category> readAllCategories(List<SimpleCategory> categories) {
    return categoriesReader.readAllCategories(categories);
  }

  @Override
  public List<Product> readAllProductsForCategories(List<SimpleCategory> categories) {
    log.info("readAllProductsForCategories({})", categories);
    throw new RuntimeException("Not implemented yet");
  }

  @Override
  public List<Product> readAllProducts(List<ProductId> products) {
    log.debug("readAllProducts({})", products);
    return products.stream().map(this::readProduct).collect(Collectors.toList());
  }

  private Product readProduct(ProductId prod) {
    log.info("readProduct({})", prod);
    try {
      return productsReader.getProduct(prod.getProductId(), prod.getLangIso());
    } catch(Exception e) {
      log.error("Unable to load product: {}", prod, e);
      throw new IllegalStateException(e.getMessage());
    }
  }

  @Override
  public List<Manufacturer> readAllManufacturers() {
    throw new NotImplementedException();
  }

  @Override
  public List<ProductPriceStrategies> readProductsPriceStrategies(List<ProductId> products) {
    log.debug("readProductsPriceStrategies({})", products);
    List<ProductPriceStrategies> strategies = new ArrayList<>();
    for (ProductId product : products) {
      strategies.add(readProductPriceStrategies(product.getProductId()));
    }
    return strategies;
  }

  @Override
  public ProductPriceStrategies readProductPriceStrategies(Long productId) {
    log.debug("readProductPriceStrategies({})", productId);
    return productsReader.getProductPriceStrategy(productId);
  }
}