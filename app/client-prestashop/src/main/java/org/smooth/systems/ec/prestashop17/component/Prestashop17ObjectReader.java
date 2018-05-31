package org.smooth.systems.ec.prestashop17.component;

import java.util.List;

import org.smooth.systems.ec.client.api.SimpleCategory;
import org.smooth.systems.ec.client.api.SimpleProduct;
import org.smooth.systems.ec.client.api.MigrationClientConstants;
import org.smooth.systems.ec.client.api.MigrationSystemReader;
import org.smooth.systems.ec.migration.model.Category;
import org.smooth.systems.ec.migration.model.Product;
import org.smooth.systems.ec.migration.model.User;
import org.smooth.systems.ec.prestashop17.api.Prestashop17Constants;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@ConditionalOnProperty(prefix = Prestashop17Constants.PRESTASHOP17_CONFIG_PREFIX, name = MigrationClientConstants.MIGRATION_CLIENT_BASE_URL)
public class Prestashop17ObjectReader extends AbstractPrestashop17Connector implements MigrationSystemReader {

  @Override
  public List<User> readAllUsers() {
    log.debug("readAllUsers()");
    List<User> users = readWebUsers();
    users.addAll(readAdministrativeUsers());
    return users;
  }

  @Override
  public List<User> readWebUsers() {
    log.debug("readWebUsers()");
    throw new RuntimeException("Not implemented yet");
  }

  @Override
  public List<User> readAdministrativeUsers() {
    log.debug("readAdministrativeUsers()");
    throw new RuntimeException("Not implemented yet");
  }

  @Override
  public List<Category> readAllCategories(List<SimpleCategory> categories) {
    log.debug("readAllCategories({})", categories);
    
    throw new RuntimeException("Not implemented yet");
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