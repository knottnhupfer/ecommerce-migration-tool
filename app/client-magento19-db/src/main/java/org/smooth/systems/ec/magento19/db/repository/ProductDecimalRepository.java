package org.smooth.systems.ec.magento19.db.repository;

import org.smooth.systems.ec.magento19.db.model.Magento19ProductDecimal;
import org.springframework.data.repository.Repository;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions> on 09.02.18.
 */
public interface ProductDecimalRepository extends Repository<Magento19ProductDecimal, Long> {

  Magento19ProductDecimal findByProductIdAndAttributeId(Long productId, Long attributeId);
}