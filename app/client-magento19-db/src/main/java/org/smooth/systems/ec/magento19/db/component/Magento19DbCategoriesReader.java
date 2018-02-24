package org.smooth.systems.ec.magento19.db.component;

import lombok.extern.slf4j.Slf4j;
import org.smooth.systems.ec.client.api.CategoryConfig;
import org.smooth.systems.ec.client.api.MigrationSystemReader;
import org.smooth.systems.ec.magento19.db.Magento19DbConstants;
import org.smooth.systems.ec.magento19.db.model.Magento19Category;
import org.smooth.systems.ec.magento19.db.repository.CategoryRepository;
import org.smooth.systems.ec.migration.model.Category;
import org.smooth.systems.ec.migration.model.Product;
import org.smooth.systems.ec.migration.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions> on 09.02.18.
 */
@Slf4j
@Component
public class Magento19DbCategoriesReader {

	@Autowired
	private CategoryRepository categoryRepo;

	@Autowired
	private Magento19DbCategoriesPopulater categoriesPopulater;

	public List<Category> readAllCategories(List<CategoryConfig> categories) {
		log.info("readAllCategories({})", categories);
		List<Category> retrievedCategories = new ArrayList<>();
		categories.forEach(config -> {
			categoriesPopulater.setLanguageCode(config.getCategoryLanguage());
			Category rootCategory = retrieveCategoriesFromRootCategoryId(config.getCategoryId());
			log.debug("Retrieved categories: {}", rootCategory);
			retrievedCategories.add(rootCategory);
		});
		log.info("Retrieved {} categories", retrievedCategories.size());
		return retrievedCategories;
	}

	public Category retrieveCategoriesFromRootCategoryId(Long rootCategoryId) {
		log.info("retrieveCategoriesFromRootCategoryId({})", rootCategoryId);
		Magento19Category retrievedCategories = retrieveCategoriesFromDb(rootCategoryId);
		Category convertedCategory = convertMagento19Category(retrievedCategories);
		categoriesPopulater.executeRecursive(convertedCategory);
		log.debug("Successfully converted categories ...");
		return convertedCategory;
	}

	private Magento19Category retrieveCategoriesFromDb(Long rootCategoryId) {
		log.info("retrieveCategoriesFromDb({})", rootCategoryId);
		Magento19Category rootCategory = categoryRepo.findById(rootCategoryId);
		if (rootCategory == null) {
			String msg = String.format("Unable to fetch root category with id:{}", rootCategoryId);
			log.error(msg);
			throw new RuntimeException(msg);
		}
		log.info("Found category: {}", rootCategory);
		populateWithChildCategories(rootCategory);
		return rootCategory;
	}

	private void populateWithChildCategories(Magento19Category magento19Category) {
		log.debug("populateWithChildCategories({})", magento19Category);
		List<Magento19Category> childCategories = categoryRepo.findByParentId(magento19Category.getId());
		childCategories.forEach(category -> {
			log.trace("Process child: {}", category);
			magento19Category.getChildrens().add(category);
			populateWithChildCategories(category);
		});
	}

	private Category convertMagento19Category(Magento19Category category) {
		log.debug("convertMagento19Category({})", category);
		Category cat = new Category();
		cat.setDisplay(Boolean.TRUE);
		cat.setId(category.getId());
		cat.setParentId(category.getParentId());

		category.getChildrens().forEach(child -> {
			Category convertedChild = convertMagento19Category(child);
			cat.addSubCategory(convertedChild);
		});
		log.trace("Converted to: {}", category);
		return cat;
	}
}