package org.smooth.systems.ec.utils.migration.action;

import lombok.extern.slf4j.Slf4j;
import org.smooth.systems.ec.client.api.ProductId;
import org.smooth.systems.ec.exceptions.NotFoundException;
import org.smooth.systems.ec.exceptions.NotImplementedException;
import org.smooth.systems.ec.migration.model.ProductPriceStrategies;
import org.smooth.systems.ec.utils.EcommerceUtilsActions;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions>
 */
@Slf4j
@Component
public class MigrateProductTierPricesExecutor extends AbstractProductsMigrationExecuter {

  @Override
  public String getActionName() {
    return EcommerceUtilsActions.PRODUCTS_TIER_PRICES_MIGRATION;
  }

  @Override
  public void execute() {
    initialize();
    List<ProductId> mainProductIds = retrieveMainProductIds();
    for (ProductId productIdSrcSystem : mainProductIds) {
      log.trace("Process price strategies for product with id:{}", productIdSrcSystem.getProductId());
      ProductPriceStrategies priceStrategy = reader.readProductPriceStrategies(productIdSrcSystem.getProductId());
      uploadProductPriceStrategies(priceStrategy);
    }
  }

  private void uploadProductPriceStrategies(ProductPriceStrategies priceStrategy) {
    // TODO check sku if
    // TODO check if strategies not already on destination system
    if (priceStrategy.getPriceStrategies().isEmpty()) {
      return;
    }
    log.debug("uploadProductPriceStrategies({})", priceStrategy);
    writer.writeProductPriceTier(priceStrategy);
  }
}