package org.smooth.systems.ec.magento19.db.repository;

import org.smooth.systems.ec.magento19.db.model.Magento19Product;
import org.springframework.data.repository.Repository;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions> on 09.02.18.
 */
public interface ProductRepository extends Repository<Magento19Product, Long> {

	Magento19Product findBySku(String sku);

  Magento19Product findById(Long categoryId);
}