package org.smooth.systems.ec.magento19.db.repository;

import java.util.List;

import org.smooth.systems.ec.magento19.db.model.Magento19ProductVarchar;
import org.springframework.data.repository.Repository;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions> on 09.02.18.
 */
public interface ProductVarcharRepository extends Repository<Magento19ProductVarchar, Long> {

	List<Magento19ProductVarchar> findByEntityIdAndAttributeId(Long productId, Long attributeId);
}