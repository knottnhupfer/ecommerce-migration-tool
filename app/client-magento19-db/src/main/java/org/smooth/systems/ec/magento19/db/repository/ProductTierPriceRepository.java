package org.smooth.systems.ec.magento19.db.repository;

import org.smooth.systems.ec.magento19.db.model.Magento19ProductInt;
import org.smooth.systems.ec.magento19.db.model.Magento19ProductTierPrice;
import org.springframework.data.repository.Repository;

import java.util.List;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions> on 09.02.18.
 */
public interface ProductTierPriceRepository extends Repository<Magento19ProductTierPrice, Long> {

  List<Magento19ProductTierPrice> findByProductId(Long productId);
}