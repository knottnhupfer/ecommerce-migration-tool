package org.smooth.systems.ec.utils.db.repository;

import java.util.List;

import org.smooth.systems.ec.utils.db.model.MagentoCategory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface CategoriesRepository extends Repository<MagentoCategory, Long> {

  @Query("SELECT c FROM MagentoCategory c WHERE c.id IN (:categoryIds) ORDER BY level DESC")
  List<MagentoCategory> getByCategoryIds(@Param("categoryIds") List<Long> categoryIds);
}