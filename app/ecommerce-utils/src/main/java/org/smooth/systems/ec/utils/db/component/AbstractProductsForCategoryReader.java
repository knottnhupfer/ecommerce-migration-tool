package org.smooth.systems.ec.utils.db.component;

import lombok.extern.slf4j.Slf4j;
import org.smooth.systems.ec.configuration.MigrationConfiguration;
import org.smooth.systems.ec.utils.db.api.IActionExecuter;
import org.smooth.systems.ec.utils.db.model.MagentoCategory;
import org.smooth.systems.ec.utils.db.repository.CategoriesRepository;
import org.smooth.systems.ec.utils.db.repository.ProductCategoryMappingRepository;
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
	protected MigrationConfiguration config;

	@Autowired
	protected CategoriesRepository categoriesRepo;

	@Autowired
	private ProductCategoryMappingRepository productsCategoryRepo;

	@Autowired
	protected ProductCategoryMappingRepository productCategoryMappingRepo;

	protected List<Long> retrieveProductIdsForCateoryId(Long mainCategoryId) {
		log.info("retrieveProductIdsForCateoryId({})", mainCategoryId);
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
		List<Long> categoryIds = productsCategoryRepo.getCategoryIdsforProductId(productId);
		if (categoryIds.isEmpty()) {
			String msg = String.format("No categoryIds found for productId:%s", productId);
			log.error(msg);
			throw new RuntimeException(msg);
		}
		log.trace("Found {} categories for productId {}", categoryIds.size(), productId);

		List<MagentoCategory> categories = categoriesRepo.getByCategoryIds(categoryIds);
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
}
