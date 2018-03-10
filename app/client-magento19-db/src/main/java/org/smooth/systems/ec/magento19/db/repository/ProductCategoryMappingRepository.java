package org.smooth.systems.ec.magento19.db.repository;

import java.util.List;

import org.smooth.systems.ec.magento19.db.model.Magento19ProductCategoryMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductCategoryMappingRepository extends JpaRepository<Magento19ProductCategoryMapping, Long> {

  @Query("SELECT distinct m.productId FROM Magento19ProductCategoryMapping m")
  List<Long> findAllProductIds();

  @Query("SELECT m.categoryId FROM Magento19ProductCategoryMapping m WHERE m.productId = :productId")
  List<Long> getCategoryIdsforProductId(@Param("productId") Long productId);

	@Query("SELECT distinct m.productId FROM Magento19ProductCategoryMapping m WHERE m.categoryId IN (:categoryIds)")
	List<Long> getProductIdsForCategoryIds(@Param("categoryIds") List<Long> categoryIds);
}