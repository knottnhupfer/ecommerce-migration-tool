package org.smooth.systems.ec.magento19.db.repository;

import org.smooth.systems.ec.magento19.db.model.Magento19Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions> on 09.02.18.
 */
public interface CategoryRepository extends Repository<Magento19Category, Long> {

	Magento19Category findById(Long categoryId);

	List<Magento19Category> findByParentId(Long parentId);

	@Query("SELECT c FROM Magento19Category c WHERE c.id IN (:categoryIds) ORDER BY level DESC")
  List<Magento19Category> getByCategoryIdsOrderedByLevel(@Param("categoryIds") List<Long> categoryIds);
}
