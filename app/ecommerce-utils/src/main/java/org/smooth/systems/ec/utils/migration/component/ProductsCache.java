package org.smooth.systems.ec.utils.migration.component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.smooth.systems.ec.client.api.MigrationSystemReader;
import org.smooth.systems.ec.client.api.ObjectId;
import org.smooth.systems.ec.migration.model.IProductCache;
import org.smooth.systems.ec.migration.model.Product;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

@Slf4j
public final class ProductsCache implements IProductCache {

	private MigrationSystemReader reader;

  private HashMap<Long, Product> products = new HashMap<>();

  private ProductsCache(MigrationSystemReader reader) {
  	this.reader = reader;
  }

	public static ProductsCache createProductsCache(MigrationSystemReader reader) {
		return createProductsCache(reader, Collections.emptyList(), true);
	}

  public static ProductsCache createProductsCache(MigrationSystemReader reader, List<ObjectId> productsInfo) {
    return createProductsCache(reader, productsInfo, true);
  }

  public static ProductsCache createProductsCache(MigrationSystemReader reader, List<ObjectId> productsInfo, boolean ignoreDeactivatedProducts) {
    ProductsCache cache = new ProductsCache(reader);
    cache.initializeProductsCache(productsInfo, ignoreDeactivatedProducts);
    return cache;
  }

	@Override
	public boolean existsProductWithSku(String sku) {
		List<Product> matchingProducts = products.values().stream().filter(p -> sku.equalsIgnoreCase(p.getSku())).collect(Collectors.toList());
		if(matchingProducts.size() == 0) {
			try {
				Product product = reader.readProductBySku(sku, "it");
				products.put(product.getId(), product);
				return true;
			} catch(Exception e) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean existsProductWithId(Long productId) {
		if(!products.containsKey(productId)) {
			try {
				List<ObjectId> product = Collections.singletonList(ObjectId.builder().objectId(productId).langIso("id").build());
				List<Product> retrievedProducts = reader.readAllProducts(product);
				if(retrievedProducts.isEmpty()) {
					return false;
				}
				products.put(productId, retrievedProducts.get(0));
				return true;
			} catch (Exception e) {
				return false;
			}
		}
		return products.containsKey(productId);
	}

  @Override
  public Product getProductById(Long productId) {
    log.trace("getProductById({})", productId);
		Assert.isTrue(existsProductWithId(productId), String.format("Unable to get product from cache with productId: %d", productId));
    return products.get(productId);
  }

	@Override
	public Product getProductBySku(String sku) {
		log.trace("getProductBySku({})", sku);
		Assert.isTrue(existsProductWithSku(sku), String.format("Unable to get product from cache with sku: %s", sku));
		List<Product> matchingProducts = products.values().stream().filter(p -> sku.equalsIgnoreCase(p.getSku())).collect(Collectors.toList());
		Assert.isTrue(matchingProducts.size() > 0, String.format("No product found for sku: %s", sku));
		return matchingProducts.get(0);
	}

  private void initializeProductsCache(List<ObjectId> productsInfo, boolean ignoreDeactivatedProducts) {
    log.info("initializeProductsCache({})", productsInfo);
    List<Product> retrievedProducts = reader.readAllProducts(productsInfo);
    for(Product prod : retrievedProducts) {
      if (products.containsKey(prod.getId())) {
        log.error("Ignore already fetched product with id {}", prod.simpleDescription());
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
