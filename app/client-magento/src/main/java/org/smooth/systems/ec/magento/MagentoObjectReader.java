package org.smooth.systems.ec.magento;

import java.util.ArrayList;
import java.util.List;

import org.smooth.systems.ec.client.api.SimpleCategory;
import org.smooth.systems.ec.client.api.SimpleProduct;
import org.smooth.systems.ec.client.api.MigrationSystemReader;
import org.smooth.systems.ec.magento.mapper.MagentoCategoryConvert;
import org.smooth.systems.ec.migration.model.Category;
import org.smooth.systems.ec.migration.model.Product;
import org.smooth.systems.ec.migration.model.IProductMetaData;
import org.smooth.systems.ec.migration.model.User;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.github.chen0040.magento.MagentoClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@ConditionalOnProperty(prefix = "migration.magento2", name = "base-url")
public class MagentoObjectReader extends AbstractMagentoConnector implements MigrationSystemReader {

  private final MagentoCategoryConvert categoryConverter;

  public MagentoObjectReader(Magento2ConnectorConfiguration config, MagentoCategoryConvert converter) {
    super(config);
    this.categoryConverter = converter;
  }

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
  public List<Category> readAllCategories(List<SimpleCategory> categoriesConfig) {
    log.info("readAllCategories({})", categoriesConfig);
    MagentoClient client = getClient();

    List<Category> res = new ArrayList<>();
    for (SimpleCategory config : categoriesConfig) {
      log.info("Read root category with id: {}", config.getCategoryId());
      com.github.chen0040.magento.models.Category category = client.categories().getRootCategoryById(config.getCategoryId());
      Assert.notNull(category, "Root category retrieving failed");

      Category cat = categoryConverter.convertCategory(category, config.getCategoryLanguage());
      Assert.notNull(cat, "Category conversion failed");

      log.info("Retrieved category: {}", cat);
      res.add(cat);
    }
    return res;
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

	@Override
	public List<IProductMetaData> readAllProductsMetaData() {
		log.debug("readAllProductsMetaData()");
		throw new RuntimeException("Not implemented yet");
	}
}
