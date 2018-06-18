package org.smooth.systems.ec.magento19.db.repository;

import org.smooth.systems.ec.magento19.db.model.Magento19ProductInt;
import org.smooth.systems.ec.magento19.db.model.Magento19ProductVarchar;
import org.springframework.data.repository.Repository;

import java.util.List;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions> on 09.02.18.
 */
public interface ProductIntRepository extends Repository<Magento19ProductInt, Long> {

  Long PRODUCT_ATTRIBUTE_ID_ACTIVATED = 80L;

  Integer PRODUCT_ATTRIBUTE_ACTIVATED_TRUE = 1;
  // Integer PRODUCT_ATTRIBUTE_ACTIVATED_FALSE = 2;

  List<Magento19ProductInt> findByProductIdAndAttributeId(Long productId, Long attributeId);
}