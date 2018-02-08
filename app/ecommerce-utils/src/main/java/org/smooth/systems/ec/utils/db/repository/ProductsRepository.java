package org.smooth.systems.ec.utils.db.repository;

import org.smooth.systems.ec.utils.db.model.MagentoProduct;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductsRepository extends Repository<MagentoProduct, Long> {

	@Query("SELECT p FROM MagentoProduct p WHERE p.id IN (:productIds) ORDER BY id ASC")
	List<MagentoProduct> getAllProductsForIds(@Param("productIds") List<Long> productIds);
}