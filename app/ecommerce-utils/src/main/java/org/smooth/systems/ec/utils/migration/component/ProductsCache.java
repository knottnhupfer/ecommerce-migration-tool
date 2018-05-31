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

  public static ProductsCache buildProductsCache(MigrationSystemReader reader, List<SimpleProduct> productsInfo) {
    ProductsCache cache = new ProductsCache();
    cache.initializeProductsCache(reader, productsInfo);
    return cache;
  }

  public Product getProductById(Long productId) {
    log.trace("getProductById({})", productId);
    if (!products.containsKey(productId)) {
      throw new IllegalArgumentException(String.format("Unable to get product from cache with id: %d", productId));
    }
    return products.get(productId);
  }

  private void initializeProductsCache(MigrationSystemReader reader, List<SimpleProduct> productsInfo) {
    log.info("initializeProductsCache({})", productsInfo);
    List<Product> retrievedProducts = reader.readAllProducts(productsInfo);
    retrievedProducts.forEach(prod -> {
      if (products.containsKey(prod.getId())) {
        log.error("Cached product: {}", products.get(prod.getId()));
        log.error("Retrieved product: {}", prod);
        throw new IllegalStateException(String.format("Unable to cache product with id: %d.", prod.getId()));
      }
      products.put(prod.getId(), prod);
    });
  }
}
