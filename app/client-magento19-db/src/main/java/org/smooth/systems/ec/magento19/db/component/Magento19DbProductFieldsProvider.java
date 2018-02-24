package org.smooth.systems.ec.magento19.db.component;

import org.smooth.systems.ec.magento19.db.model.Magento19EavAttributeOption;
import org.smooth.systems.ec.magento19.db.model.Magento19ProductIndexEav;
import org.smooth.systems.ec.magento19.db.repository.EavAttributeOptionsRepository;
import org.smooth.systems.ec.magento19.db.repository.ProductEavIndexRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions> on
 * 24.02.18.
 */
@Slf4j
@Component
public class Magento19DbProductFieldsProvider {

  @Autowired
  private ProductEavIndexRepository productEavIndexRepo;

  @Autowired
  private EavAttributeOptionsRepository eavAttributeRepo;

  public String getBrandIdForProduct(Long productId) {
    Assert.notNull(productId, "productId is null");
    Magento19ProductIndexEav indexEav = productEavIndexRepo.findManufacturerEntryForProductId(productId);
    if (indexEav == null) {
      log.warn("Unable to find product eav for productId: {}", productId);
      return null;
    }

    Magento19EavAttributeOption productAttribute = eavAttributeRepo.findByOptionId(Long.parseLong(indexEav.getValue()));
    if (productAttribute == null) {
      log.warn("Unable to find product index eav for productId: {}", productId);
      return null;
    }
    return productAttribute.getValue();
  }
}