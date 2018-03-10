package org.smooth.systems.ec.magento19.db.repository;

import org.smooth.systems.ec.magento19.db.model.Magento19EavAttributeOption;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions> on 09.02.18.
 */
public interface EavAttributeOptionsRepository extends Repository<Magento19EavAttributeOption, Long> {

  @Query("SELECT a FROM Magento19EavAttributeOption a WHERE a.optionId = :optionId")
  Magento19EavAttributeOption findByOptionId(@Param("optionId") Long optionId);
}