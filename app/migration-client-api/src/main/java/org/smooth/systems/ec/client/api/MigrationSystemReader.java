package org.smooth.systems.ec.client.api;

import java.util.List;
import java.util.Optional;

import org.smooth.systems.ec.exceptions.NotImplementedException;
import org.smooth.systems.ec.migration.model.*;

public interface MigrationSystemReader extends RegisterableComponent {

  /**
   * Retrieves all users
   *
   * @return list of all users in the system, web shop and administrative users
   */
  public List<User> readAllUsers();

  /**
   * Retrieves all web shop users
   *
   * @return list of all web shop users in the system, admin users excluded
   */
  public List<User> readWebUsers();

  /**
   * Retrieves all administrative users
   *
   * @return list of all administrative users in the system
   */
  public List<User> readAdministrativeUsers();

  /**
   * Retrieves a complete list of all root categories incl. sub categories
   *
   * @param categories
   *          TODO
   * @return list of root categories which includes sub categories as well
   */
  public List<Category> readAllCategories(List<SimpleCategory> categories);

  /**
   * Retrieves a complete list of products of a specific category incl. sub
   * categories
   *
   * @param categories
   *          category ids to be searched in
   * @return list of products of the specified category
   */
  List<Product> readAllProductsForCategories(List<SimpleCategory> categories);

  List<Product> readAllProducts(List<ProductId> products);

  List<Manufacturer> readAllManufacturers();

  List<ProductPriceStrategies> readProductsPriceStrategies(List<ProductId> products);

  ProductPriceStrategies readProductPriceStrategies(Long productId);

  List<IProductMetaData> readAllProductsMetaData();

	default List<Long> readAllProductsImagesProductIds() {
		throw new NotImplementedException();
	}

  default List<Long> readAllProductSpecificPrices() {
    throw new NotImplementedException();
  }

	default Product readProductBySku(String sku, String langCode) {
		throw new NotImplementedException();
	}
}
