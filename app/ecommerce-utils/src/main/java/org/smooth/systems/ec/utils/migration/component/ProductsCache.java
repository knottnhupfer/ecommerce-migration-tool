package org.smooth.systems.ec.utils.migration.component;

import java.util.HashMap;
import java.util.List;

import org.smooth.systems.ec.client.api.MigrationSystemReader;
import org.smooth.systems.ec.client.api.SimpleProduct;
import org.smooth.systems.ec.migration.model.Product;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ProductsCache {

  private HashMap<Long, Product> products = new HashMap<>();

  private ProductsCache() {
  }

  public static ProductsCache createProductsCache(MigrationSystemReader reader, List<SimpleProduct> productsInfo) {
    return createProductsCache(reader, productsInfo, true);
  }

  public static ProductsCache createProductsCache(MigrationSystemReader reader, List<SimpleProduct> productsInfo, boolean ignoreDeactivatedProducts) {
    ProductsCache cache = new ProductsCache();
    cache.initializeProductsCache(reader, productsInfo, ignoreDeactivatedProducts);
    return cache;
  }

  public Product getProductById(Long productId) {
    log.trace("getProductById({})", productId);
    if (!products.containsKey(productId)) {
      throw new IllegalArgumentException(String.format("Unable to get product from cache with id: %d", productId));
    }
    return products.get(productId);
  }

  private void initializeProductsCache(MigrationSystemReader reader, List<SimpleProduct> productsInfo, boolean ignoreDeactivatedProducts) {
    log.info("initializeProductsCache({})", productsInfo);
    List<Product> retrievedProducts = reader.readAllProducts(productsInfo);
    for(Product prod : retrievedProducts) {
      if (products.containsKey(prod.getId())) {
        log.error("Cached product: {}", products.get(prod.getId()));
        log.error("Retrieved product: {}", prod);
        throw new IllegalStateException(String.format("Unable to cache product with id: %d.", prod.getId()));
      }
      if(ignoreDeactivatedProducts && !prod.getActivated()) {
        log.warn("Ignore deactivated product {}", prod.simpleDescription());
        continue;
      }
      products.put(prod.getId(), prod);
    }
  }
}
