package org.smooth.systems.ec.magento19.db.component;

import org.smooth.systems.ec.client.api.SimpleCategory;
import org.smooth.systems.ec.client.api.SimpleProduct;
import org.smooth.systems.ec.client.api.MigrationSystemReader;
import org.smooth.systems.ec.exceptions.NotImplementedException;
import org.smooth.systems.ec.magento19.db.Magento19Constants;
import org.smooth.systems.ec.migration.model.Category;
import org.smooth.systems.ec.migration.model.Product;
import org.smooth.systems.ec.migration.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

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
    log.debug("readAllProductsForCategories({})", categories);
    throw new RuntimeException("Not implemented yet");
  }

  @Override
  public List<Product> readAllProducts(List<SimpleProduct> products) {
    log.debug("readAllProducts({})", products);
    throw new RuntimeException("Not implemented yet");
  }
}