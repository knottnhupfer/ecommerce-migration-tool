package org.smooth.systems.ec.dummy.component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.smooth.systems.ec.client.api.MigrationSystemReader;
import org.smooth.systems.ec.client.api.SimpleCategory;
import org.smooth.systems.ec.client.api.ProductId;
import org.smooth.systems.ec.dummy.DummyConstants;
import org.smooth.systems.ec.exceptions.NotImplementedException;
import org.smooth.systems.ec.migration.model.*;
import org.springframework.stereotype.Component;

@Component
public class DummyMigrationSystemReader implements MigrationSystemReader {

	private Map<Long, Product> dummyProducts = new HashMap<>();

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
		throw new NotImplementedException();
	}

	@Override
	public List<Product> readAllProductsForCategories(List<SimpleCategory> categories) {
		throw new NotImplementedException();
	}

	@Override
	public List<Product> readAllProducts(List<ProductId> productsInfo) {
		List<Product> products = new ArrayList<>();
		for(ProductId productInfo : productsInfo) {
			if(!dummyProducts.containsKey(productInfo.getProductId())) {
				throw new IllegalStateException("Unable to find product for id: " + productInfo.getProductId());
			}
			products.add(dummyProducts.get(productInfo.getProductId()));
		}
		return products;
	}

	@Override
	public String getName() {
		return DummyConstants.DUMMY_PROFILE;
	}

	@PostConstruct
	public void initialize() {
		for (int i = 1; i < 6; i++) {
			initializeProducts(new Long(i));
		}
	}

	private void initializeProducts(Long id) {
		dummyProducts.put(id, createProduct(id, String.valueOf(id), "it"));
		Long altId = new Long(1000 + id);
		dummyProducts.put(altId, createProduct(altId, "_" + String.valueOf(id), "de"));
	}

	private Product createProduct(Long id, String sku, String isoCode) {
		Product product = new Product(id, sku, LocalDateTime.now());
		ProductTranslateableAttributes attribute = new ProductTranslateableAttributes(isoCode);
		attribute.setName("name_" + isoCode + "_" + id);
		attribute.setFriendlyUrl("friendly_url_" + isoCode + "_" + id);
		attribute.setDescription("description_" + isoCode + "_" + id);

		List<Long> categoryIds = new ArrayList<>();
		categoryIds.add(new Long(id));
		product.setCategories(categoryIds);

		List<String> urls = new ArrayList<>();
		urls.add(String.format("/%d/%d.jpg", id, 1));
		urls.add(String.format("/%d/%d.jpg", id, 2));
		urls.add(String.format("/%d/%d.jpg", id, 3));
		product.setProductImageUrls(urls);

		List<ProductTranslateableAttributes> attributes = new ArrayList<>();
		attributes.add(attribute);
		product.setAttributes(attributes);
//		log.info("Created dummy project: {}", product);
		return product;
	}

  @Override
  public List<Manufacturer> readAllManufacturers() {
    throw new NotImplementedException();
  }

	@Override
	public List<ProductPriceStrategies> readProductsPriceStrategies(List<ProductId> products) {
		throw new NotImplementedException();
	}

	@Override
	public ProductPriceStrategies readProductPriceStrategies(Long productId) {
		throw new NotImplementedException();
	}
}