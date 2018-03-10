package org.smooth.systems.ec.magento19.db.repository;

import org.smooth.systems.ec.magento19.db.model.Magento19ProductVisibility;
import org.springframework.data.repository.Repository;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions> on 09.02.18.
 */
public interface ProductVisibilityRepository extends Repository<Magento19ProductVisibility, Long> {

  Magento19ProductVisibility findById(Long categoryId);
}