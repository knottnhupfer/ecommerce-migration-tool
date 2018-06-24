package org.smooth.systems.ec.utils.migration.action;

import lombok.extern.slf4j.Slf4j;
import org.smooth.systems.ec.exceptions.NotFoundException;
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
      try {
        uploadProductPriceStrategies(priceStrategy);
      } catch (NotFoundException e) {
        log.error("Unable to map product tier price to destination system: {}", priceStrategy, e);
        throw new IllegalStateException("Unable to map product tier price to destination system.");
      }
    }

  }

  private void uploadProductPriceStrategies(ProductPriceStrategies priceStrategy) throws NotFoundException {
    log.debug("uploadProductPriceStrategies({})", priceStrategy);
    productIdsMigration.getMappedIdForId(priceStrategy.getProductId());
    writer.writeProductPriceTier(priceStrategy);
  }

  protected void initialize() {
    super.initialize();
    productIdsMigration.initializeIdMapperFromFile();
  }
}