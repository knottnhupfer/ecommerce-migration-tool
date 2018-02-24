package org.smooth.systems.ec.utils.db.component;

import lombok.extern.slf4j.Slf4j;
import org.smooth.systems.ec.utils.db.api.IActionExecuter;
import org.smooth.systems.ec.utils.db.model.MagentoCategory;
import org.smooth.systems.ec.utils.db.model.MagentoProduct;
import org.smooth.systems.ec.utils.db.repository.CategoriesRepository;
import org.smooth.systems.ec.utils.db.repository.ProductCategoryMappingRepository;
import org.smooth.systems.ec.utils.db.repository.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions> on 03.02.18.
 */
@Slf4j
public abstract class AbstractProductsForCategoryReader implements IActionExecuter {

	@Autowired
	protected ProductsRepository productsRepo;

	@Autowired
	protected CategoriesRepository categoriesRepo;

	@Autowired
	protected ProductCategoryMappingRepository productCategoryMappingRepo;

	protected List<Long> retrieveAllProductIdsForCateoryId(Long mainCategoryId) {
		log.info("retrieveAllProductIdsForCateoryId({})", mainCategoryId);
		List<Long> categoryIds = getSubCategoryIdsForCategoryId(mainCategoryId);
		log.info("Retrieved category ids: {}", categoryIds);
		return getProductsForCategoryIds(categoryIds);
	}

	private List<Long> getSubCategoryIdsForCategoryId(Long categoryId) {
		ArrayList<Long> categoryIds = new ArrayList<>();
		categoryIds.add(categoryId);
		List<MagentoCategory> subCategories = categoriesRepo.findByParentId(categoryId);
		List<Long> subCategoryIds = subCategories.stream().map(cat -> cat.getId()).collect(Collectors.toList());
		for (Long subCategoryId: subCategoryIds) {
			categoryIds.addAll(getSubCategoryIdsForCategoryId(subCategoryId));
		}
		return categoryIds;
	}

	private List<Long> getProductsForCategoryIds(List<Long> categoryIds) {
		return productCategoryMappingRepo.getProductIdsForCategoryIds(categoryIds);
	}

	protected Long getCategoryIdForProductId(Long productId) {
		log.debug("getCategoryIdForProductId({})", productId);
		List<Long> categoryIds = productCategoryMappingRepo.getCategoryIdsforProductId(productId);
		if (categoryIds.isEmpty()) {
			String msg = String.format("No categoryIds found for productId:%s", productId);
			log.error(msg);
			throw new RuntimeException(msg);
		}
		log.trace("Found {} categories for productId {}", categoryIds.size(), productId);

		List<MagentoCategory> categories = categoriesRepo.getByCategoryIdsOrderedByLevel(categoryIds);
		if (categories.isEmpty()) {
			String msg = String.format("No categories found for productId:%s", productId);
			log.error(msg);
			throw new RuntimeException(msg);
		}
		if (categoryIds.size() != categories.size()) {
			log.warn("Unable to fetch proper category size({}), found categories: {}", categoryIds.size(), categories);
		}
		log.trace("Found {} categories for productId {}", categoryIds.size(), productId);

		return categories.get(0).getId();
	}

	protected List<MagentoProduct> getCategoryProducts(Long categoryId) {
		log.info("getCategoryProducts({})", categoryId);
		List<Long> rootProductIds = retrieveAllProductIdsForCateoryId(categoryId);
		log.info("Retrieved {} product ids for category id {}", rootProductIds.size(), categoryId);
		return productsRepo.getAllProductsForIds(rootProductIds);
	}
}
