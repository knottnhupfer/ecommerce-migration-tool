package org.smooth.systems.ec.utils.migration.component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.smooth.systems.ec.client.api.MigrationSystemReader;
import org.smooth.systems.ec.client.api.ProductId;
import org.smooth.systems.ec.migration.model.Product;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class ProductsCache {

	private MigrationSystemReader reader;

  private HashMap<Long, Product> products = new HashMap<>();

  private ProductsCache(MigrationSystemReader reader) {
  	this.reader = reader;
  }

	public static ProductsCache createProductsCache(MigrationSystemReader reader) {
		return createProductsCache(reader, Collections.emptyList(), true);
	}

  public static ProductsCache createProductsCache(MigrationSystemReader reader, List<ProductId> productsInfo) {
    return createProductsCache(reader, productsInfo, true);
  }

  public static ProductsCache createProductsCache(MigrationSystemReader reader, List<ProductId> productsInfo, boolean ignoreDeactivatedProducts) {
    ProductsCache cache = new ProductsCache(reader);
    cache.initializeProductsCache(productsInfo, ignoreDeactivatedProducts);
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
			return reader.readProductBySku(sku, "it");
		} else if(matchingProducts.size() != 1) {
			String msg = String.format("Found more then 1 product [size=%d] with sku '%s'", matchingProducts.size(), sku);
			log.error(msg);
			throw new IllegalStateException(msg);
		}
		return matchingProducts.get(0);
	}

  private void initializeProductsCache(List<ProductId> productsInfo, boolean ignoreDeactivatedProducts) {
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
