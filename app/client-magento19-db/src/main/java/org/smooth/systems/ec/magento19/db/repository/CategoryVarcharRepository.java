package org.smooth.systems.ec.magento19.db.repository;

import java.util.List;

import org.smooth.systems.ec.magento19.db.model.Magento19CategoryVarchar;
import org.springframework.data.repository.Repository;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions> on 09.02.18.
 */
public interface CategoryVarcharRepository extends Repository<Magento19CategoryVarchar, Long> {

	List<Magento19CategoryVarchar> findByEntityId(Long categoryId);
}
