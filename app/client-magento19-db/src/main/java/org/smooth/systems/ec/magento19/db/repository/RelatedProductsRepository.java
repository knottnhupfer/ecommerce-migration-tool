package org.smooth.systems.ec.magento19.db.repository;

import org.smooth.systems.ec.magento19.db.model.Magento19RelatedProduct;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions> on 09.02.18.
 */
public interface RelatedProductsRepository extends Repository<Magento19RelatedProduct, Long> {

	String SELECT_RELATED_PRODUCT = "select link_id, product_id, linked_product_id FROM catalog_product_link"
		+ " LEFT JOIN catalog_product_entity ON catalog_product_entity.entity_id = catalog_product_link.product_id"
		+ " WHERE catalog_product_link.product_id = ?1";

	@Query(value = SELECT_RELATED_PRODUCT, nativeQuery = true)
	List<Magento19RelatedProduct> findRelatedProductsByProductId(Long id);
}