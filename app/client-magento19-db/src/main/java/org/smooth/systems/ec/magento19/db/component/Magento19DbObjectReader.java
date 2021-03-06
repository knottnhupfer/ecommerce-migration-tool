package org.smooth.systems.ec.magento19.db.component;

import org.smooth.systems.ec.client.api.ObjectId;
import org.smooth.systems.ec.client.api.SimpleCategory;
import org.smooth.systems.ec.client.api.MigrationSystemReader;
import org.smooth.systems.ec.exceptions.NotImplementedException;
import org.smooth.systems.ec.magento19.db.Magento19Constants;
import org.smooth.systems.ec.magento19.db.model.Magento19RelatedProduct;
import org.smooth.systems.ec.magento19.db.repository.RelatedProductsRepository;
import org.smooth.systems.ec.migration.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
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

	@Autowired
	private RelatedProductsRepository relatedProductsRepository;

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
  public List<Product> readAllProducts(List<ObjectId> products) {
    log.debug("readAllProducts({})", products);
    return products.stream().map(this::readProduct).collect(Collectors.toList());
  }

  private Product readProduct(ObjectId prod) {
    log.info("readProduct({})", prod);
    try {
      return productsReader.getProduct(prod.getObjectId(), prod.getLangIso());
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
  public List<ProductPriceStrategies> readProductsPriceStrategies(List<ObjectId> products) {
    log.debug("readProductsPriceStrategies({})", products);
    List<ProductPriceStrategies> strategies = new ArrayList<>();
    for (ObjectId product : products) {
      strategies.add(readProductPriceStrategies(product.getObjectId()));
    }
    return strategies;
  }

  @Override
  public ProductPriceStrategies readProductPriceStrategies(Long productId) {
    log.debug("readProductPriceStrategies({})", productId);
    return productsReader.getProductPriceStrategy(productId);
  }

	@Override
	public List<IProductMetaData> readAllProductsMetaData() {
		log.debug("readAllProductsMetaData()");
		throw new RuntimeException("Not implemented yet");
	}

	@Override
	public Product readProductBySku(String sku, String langCode) {
		log.debug("readProductBySku({}, {})", sku, langCode);
		return productsReader.getProduct(sku, langCode);
	}

	@Override
	public RelatedProducts readRelatedProduct(IProductCache productCache, Product product) {
		RelatedProducts relatedProducts = new RelatedProducts(product.getSku(), product.getId());
		List<Magento19RelatedProduct> relatedProductsList = relatedProductsRepository.findRelatedProductsByProductId(relatedProducts.getProduct().getSrcProductId());

		relatedProductsList.forEach(relatedProduct -> {
			Long prodId = relatedProduct.getRelatedProductId();
			relatedProducts.addRelatedProduct(new ProductMapping(productCache.getProductById(prodId).getSku(), prodId));
		});
		return relatedProducts;
	}

	@Override
	public List<ObjectId> readProductIdsFromCategoryIdRecursively(final ObjectId rootCategory) {
  	log.info("readProductIdsFromCategoryIdRecursively({})", rootCategory);
		SimpleCategory rootCategoryObj = new SimpleCategory(rootCategory.getObjectId(), rootCategory.getLangIso());
		List<Category> categories = categoriesReader.readAllCategories(Collections.singletonList(rootCategoryObj));
//		Category category = categoriesReader.retrieveCategoriesFromRootCategoryId(rootCategory.getObjectId());
		// TODO
//		productsReader.getProduct()
//		List<Category> childrens = category.getChildrens();
		throw new NotImplementedException();
	}
}
