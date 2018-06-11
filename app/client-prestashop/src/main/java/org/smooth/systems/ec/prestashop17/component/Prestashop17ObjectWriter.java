package org.smooth.systems.ec.prestashop17.component;

import java.io.File;
import java.util.List;

import org.smooth.systems.ec.client.api.MigrationClientConstants;
import org.smooth.systems.ec.client.api.MigrationSystemWriter;
import org.smooth.systems.ec.client.util.ObjectIdMapper;
import org.smooth.systems.ec.configuration.MigrationConfiguration;
import org.smooth.systems.ec.exceptions.NotImplementedException;
import org.smooth.systems.ec.exceptions.ObjectAlreadyExistsException;
import org.smooth.systems.ec.migration.model.AbstractCategoryWriter;
import org.smooth.systems.ec.migration.model.Category;
import org.smooth.systems.ec.migration.model.Manufacturer;
import org.smooth.systems.ec.migration.model.Product;
import org.smooth.systems.ec.migration.model.User;
import org.smooth.systems.ec.prestashop17.api.Prestashop17Constants;
import org.smooth.systems.ec.prestashop17.client.Prestashop17Client;
import org.smooth.systems.ec.prestashop17.mapper.CategoryMapper;
import org.smooth.systems.ec.prestashop17.model.ProductConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@ConditionalOnProperty(prefix = Prestashop17Constants.PRESTASHOP17_CONFIG_PREFIX, name = MigrationClientConstants.MIGRATION_CLIENT_BASE_URL)
public class Prestashop17ObjectWriter extends AbstractPrestashop17Connector implements MigrationSystemWriter {

  public static final Long PRESTASHOP_ROOT_CATEGORY_ID = 2L;

  private final PrestashopLanguageTranslatorCache languagesCache;

  private CategoryWriter categoryWriter;

  @Autowired
  public Prestashop17ObjectWriter(MigrationConfiguration config, PrestashopLanguageTranslatorCache languagesCache,
      Prestashop17Client client) {
    super(config, client);
    this.languagesCache = languagesCache;
  }

  @Override
  public void writeUsers(List<User> customers) throws ObjectAlreadyExistsException {
    log.info("writeUsers({})", customers.size());
    writeUsers(customers, false);
  }

  @Override
  public void writeUsers(List<User> customers, boolean updateIfExists) throws ObjectAlreadyExistsException {
    log.info("writeUsers({}, {})", customers.size(), updateIfExists);
    throw new RuntimeException("Not implemented yet");
  }

  @Override
  public void writeCategories(Category rootCategory) throws ObjectAlreadyExistsException {
    log.info("writeCategory({})", rootCategory.getAttributes().get(0).getName());
    writeCategories(rootCategory, false);
  }

  @Override
  public void writeCategories(Category rootCategory, boolean updateIfExists) throws ObjectAlreadyExistsException {
    log.info("writeCategories({}, {})", rootCategory, updateIfExists);
    log.info("writeCategories({})", rootCategory.getAttributes().get(0).getName());

    if (!updateIfExists) {
      if (!client.getCategoriesMetaData().isEmpty()) {
        throw new ObjectAlreadyExistsException("Some categories already exists on system, skipping write categories.");
      }
    }

    CategoriesRepairAndValidator validator = new CategoriesRepairAndValidator();
    validator.executeRecursive(rootCategory);

    categoryWriter = new CategoryWriter();
    rootCategory.getChildrens().forEach(category -> {
      categoryWriter.executeRecursive(category);
    });
  }

  @Override
  public ObjectIdMapper getCategoriesObjectIdMapper() {
    return categoryWriter.getMapper();
  }

  @Override
  public ObjectIdMapper getProductsObjectIdMapper() {
    throw new NotImplementedException();
  }

	@Override
	public Product writeProduct(Product product) {
    log.info("writeProduct({})", product);
    org.smooth.systems.ec.prestashop17.model.Product prod = ProductConvertUtil.convertProduct(languagesCache, product);
    prod = client.writeProduct(prod);
    product.setId(prod.getId());
    return product;
	}

	@Override
	public void uploadProductImages(Long prodId, File productImage) {
		throw new NotImplementedException();
	}

  @Override
  public Manufacturer writeManufacturer(String manufacturerName) {
    log.info("writeBrand({})", manufacturerName);
    org.smooth.systems.ec.prestashop17.model.Manufacturer manufacturer = new org.smooth.systems.ec.prestashop17.model.Manufacturer();
    manufacturer.setId(null);
    manufacturer.setName(manufacturerName);
    manufacturer = client.writeManufacturer(manufacturer);
    return manufacturer.convert();
  }

  @Override
  public void repairAndValidateData() {

    // TODO check all category data
  }

  private class CategoryWriter extends AbstractCategoryWriter {

    private long counter;

    public CategoryWriter() {
      super(config);
      counter = 0;
    }

    @Override
    protected Long writeCategory(Category category, int level) {
      org.smooth.systems.ec.prestashop17.model.Category cat = CategoryMapper.convertCategoryToSystemModel(languagesCache, category,
          false);
      log.info("Write category[{}]: {}", ++counter, cat);
      org.smooth.systems.ec.prestashop17.model.Category createdCategory = client.writeCategory(languagesCache, cat);
      return createdCategory.getId();
    }
  }
}