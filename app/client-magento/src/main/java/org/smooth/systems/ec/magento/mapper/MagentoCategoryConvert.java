package org.smooth.systems.ec.magento.mapper;

import java.util.List;

import org.smooth.systems.ec.migration.model.Category;
import org.smooth.systems.ec.migration.model.CategoryTranslateableAttributes;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@ConditionalOnProperty(prefix = "migration.magento2", name = "base-url")
public class MagentoCategoryConvert {

  public Category convertCategory(com.github.chen0040.magento.models.Category category, String language) {
    Assert.notNull(category, "category is null, unable to convert");
    Category cat = new Category();
    cat.setId(category.getId());
    cat.setDisplay(category.is_active());
    cat.setParentId(category.getParent_id());
    populateCategoryAttributes(category, cat, language);
    populateChildCategory(cat, category.getChildren_data(), language);
    return cat;
  }

  private void populateCategoryAttributes(com.github.chen0040.magento.models.Category category, Category cat, String language) {
    CategoryTranslateableAttributes attributes = new CategoryTranslateableAttributes(language);
    attributes.setName(category.getName());
    cat.addCategory(attributes);
  }

  private void populateChildCategory(Category parentCategory, List<com.github.chen0040.magento.models.Category> subCategories,
      String language) {
    if (subCategories == null) {
      return;
    }
    for (com.github.chen0040.magento.models.Category category : subCategories) {
      parentCategory.addSubCategory(convertCategory(category, language));
    }
  }
}