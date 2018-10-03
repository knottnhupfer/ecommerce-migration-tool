package org.smooth.systems.ec.utils.migration.component;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.smooth.systems.ec.client.api.MigrationSystemReader;
import org.smooth.systems.ec.client.api.ProductId;
import org.smooth.systems.ec.migration.model.Product;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ProductsCache {

  private HashMap<Long, Product> products = new HashMap<>();

  private ProductsCache() {
  }

  public static ProductsCache createProductsCache(MigrationSystemReader reader, List<ProductId> productsInfo) {
    return createProductsCache(reader, productsInfo, true);
  }

  public static ProductsCache createProductsCache(MigrationSystemReader reader, List<ProductId> productsInfo, boolean ignoreDeactivatedProducts) {
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

	public Product getProductBySku(String sku) {
		log.trace("getProductBySku({})", sku);
		List<Product> matchingProducts = products.values().stream().filter(p -> sku.equalsIgnoreCase(p.getSku())).collect(Collectors.toList());
		if(matchingProducts.isEmpty()) {
			String msg = String.format("Unable to find product with sku '%s'", sku);
			log.error(msg);
			throw new IllegalStateException(msg);
		} else if(matchingProducts.size() != 1) {
			String msg = String.format("Found more then 1 product [size=%d] with sku '%s'", matchingProducts.size(), sku);
			log.error(msg);
			throw new IllegalStateException(msg);
		}
		return matchingProducts.get(0);
	}

  private void initializeProductsCache(MigrationSystemReader reader, List<ProductId> productsInfo, boolean ignoreDeactivatedProducts) {
    log.info("initializeProductsCache({})", productsInfo);
    List<Product> retrievedProducts = reader.readAllProducts(productsInfo);
    for(Product prod : retrievedProducts) {
      if (products.containsKey(prod.getId())) {
        log.error("Ignore already fetched product with id {}", prod.simpleDescription());
//        log.error("Cached product: {}", products.get(prod.getId()));
//        log.error("Retrieved product: {}", prod);
        continue;
      }
      if(ignoreDeactivatedProducts && !prod.getActivated()) {
        log.warn("Ignore deactivated product {}", prod.simpleDescription());
        continue;
      }
      products.put(prod.getId(), prod);
    }
  }
}
