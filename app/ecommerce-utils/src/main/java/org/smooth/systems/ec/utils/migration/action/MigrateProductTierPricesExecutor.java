package org.smooth.systems.ec.utils.migration.action;

import lombok.extern.slf4j.Slf4j;
import org.smooth.systems.ec.client.api.ObjectId;
import org.smooth.systems.ec.migration.model.Product;
import org.smooth.systems.ec.migration.model.ProductPriceStrategies;
import org.smooth.systems.ec.utils.EcommerceUtilsActions;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions>
 */
@Slf4j
@Component
public class MigrateProductTierPricesExecutor extends AbstractProductsMigrationExecuter {

	public MigrateProductTierPricesExecutor() {
		super(EcommerceUtilsActions.PRODUCTS_TIER_PRICES_MIGRATION);
	}

  @Override
  public void execute() {
    initialize();

    List<ObjectId> mainProductIds = initializeSourceSystemProductCacheAndRetrieveList();
    log.info("Read products and initialized cache ({})", mainProductIds.size());

    for (ObjectId productIdSrcSystem : mainProductIds) {
      log.trace("Process price strategies for product with id:{}", productIdSrcSystem.getObjectId());

      Product product = srcProductsCache.getProductById(productIdSrcSystem.getObjectId());
      Assert.notNull(product, String.format("No product found for"));

      if (!doesProductOnDestinationSystemWithSkuExists(product.getSku()) || hasProductAlreadyPricingStrategies(product)) {
        log.warn("Skip product images for product with sku '{}', does not exists on the destination system.", product.getSku());
        continue;
      }
      ProductPriceStrategies priceStrategy = readerSrcSystem.readProductPriceStrategies(productIdSrcSystem.getObjectId());
      priceStrategy.setNetPrice(product.getNetPrice());
      Long productIdDestinationSystem = getProductIdDestinationSystemForProductSku(product.getSku());
      priceStrategy.setProductId(productIdDestinationSystem);
      uploadProductPriceStrategies(priceStrategy);
    }
  }

  private boolean hasProductAlreadyPricingStrategies(Product product) {
    // TODO check if strategies not already on destination system
		throw new IllegalStateException("NIY");
//		return false;
  }

  private void uploadProductPriceStrategies(ProductPriceStrategies priceStrategy) {
    if (priceStrategy.getPriceStrategies().isEmpty()) {
      return;
    }

    log.debug("uploadProductPriceStrategies({})", priceStrategy);
    writerDstSystem.writeProductPriceTier(priceStrategy);
  }
}