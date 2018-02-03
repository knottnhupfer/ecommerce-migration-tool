package org.smooth.systems.ec.utils.db.repository;

import java.util.List;

import org.smooth.systems.ec.utils.db.model.MagentoProductCategoryMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductCategoryMappingRepository extends JpaRepository<MagentoProductCategoryMapping, Long> {

  @Query("SELECT distinct m.productId FROM MagentoProductCategoryMapping m")
  List<Long> findAllProductIds();

  @Query("SELECT m.categoryId FROM MagentoProductCategoryMapping m WHERE m.productId = :productId")
  List<Long> findByProductId(@Param("productId") Long productId);
}