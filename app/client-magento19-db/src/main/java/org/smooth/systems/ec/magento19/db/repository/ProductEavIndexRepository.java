package org.smooth.systems.ec.magento19.db.repository;

import org.smooth.systems.ec.magento19.db.model.Magento19ProductIndexEav;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions> on 24.02.18.
 */
public interface ProductEavIndexRepository extends Repository<Magento19ProductIndexEav, Long> {

  @Query("SELECT a FROM Magento19ProductIndexEav a WHERE a.productId = :productId")
  Magento19ProductIndexEav findManufacturerEntryForProductId(@Param("productId") Long productId);
}
