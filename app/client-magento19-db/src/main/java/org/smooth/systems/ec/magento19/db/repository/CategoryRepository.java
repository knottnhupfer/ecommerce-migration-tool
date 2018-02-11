package org.smooth.systems.ec.magento19.db.repository;

import org.smooth.systems.ec.magento19.db.model.Magento19Category;
import org.springframework.data.repository.Repository;

import java.util.List;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions> on 09.02.18.
 */
public interface CategoryRepository extends Repository<Magento19Category, Long> {

	Magento19Category findById(Long categoryId);

	List<Magento19Category> findByParentId(Long parentId);
}
