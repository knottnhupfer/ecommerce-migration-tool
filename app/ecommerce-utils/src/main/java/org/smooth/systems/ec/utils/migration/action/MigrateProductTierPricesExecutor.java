package org.smooth.systems.ec.utils.migration.action;

import lombok.extern.slf4j.Slf4j;
import org.smooth.systems.ec.exceptions.NotImplementedException;
import org.smooth.systems.ec.migration.model.ProductPriceStrategies;
import org.smooth.systems.ec.utils.EcommerceUtilsActions;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions> on
 * 29.05.18.
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
    Set<Long> productIdsSourceSystem = productIdsMigration.keySet();
    for (Long productIdSrcSystem : productIdsSourceSystem) {
      log.trace("Process price strategies for product with id:{}", productIdSrcSystem);
      ProductPriceStrategies priceStrategy = reader.readProductPriceStrategies(productIdSrcSystem);
      // TODO check if strategies not already on destination system
      if (priceStrategy.getPriceStrategies().isEmpty()) {
        continue;
      }
      uploadProductPriceStrategies(priceStrategy);
    }
    // check if tier prices already exists for given product, if yes abort processing for product
    // read tier prices
    // upload tier prices
  }

  private void uploadProductPriceStrategies(ProductPriceStrategies priceStrategy) {
    log.debug("uploadProductPriceStrategies({})", priceStrategy);
    throw new NotImplementedException();
  }

  protected void initialize() {
    super.initialize();
    productIdsMigration.initializeIdMapperFromFile();
  }
}