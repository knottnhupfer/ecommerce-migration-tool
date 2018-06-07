package org.smooth.systems.ec.magento19.db.repository;

import org.smooth.systems.ec.magento19.db.model.Magento19ProductText;
import org.springframework.data.repository.Repository;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions> on 09.02.18.
 */
public interface ProductTextRepository extends Repository<Magento19ProductText, Long> {

  Magento19ProductText findByEntityIdAndAttributeIdAndStoreId(Long productId, Long attributeId, Long storeId);
}