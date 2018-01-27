package org.smooth.systems.ec.prestashop17.component;

import java.util.List;

import org.smooth.systems.ec.migration.model.AbstractCategoryRecursiveProcessor;
import org.smooth.systems.ec.migration.model.Category;
import org.smooth.systems.ec.migration.model.CategoryTranslateableAttributes;
import org.springframework.util.Assert;

public class CategoriesRepairAndValidator extends AbstractCategoryRecursiveProcessor {

  @Override
  protected void executeCategory(Category category, int level) {
    List<CategoryTranslateableAttributes> attributes = category.getAttributes();
    for (CategoryTranslateableAttributes attr : attributes) {
      if (attr.getFriendlyUrl() == null || attr.getFriendlyUrl().isEmpty()) {
        attr.setFriendlyUrl(generateFriendlyUrl(attr));
      }
      validateAttribute(category, attr);
    }
    validateMainCategoryData(category);
  }

  private void validateMainCategoryData(Category category) {
    Assert.notNull(category.getId(), "id not defined");
  }

  private void validateAttribute(Category category, CategoryTranslateableAttributes attr) {
    Assert.hasText(attr.getLangCode(), String.format("lanuage code not defined for category with id:%d", category.getId()));
    Assert.hasText(attr.getName(), String.format("name not defined for category with id:%d", category.getId()));
    Assert.hasText(attr.getFriendlyUrl(), String.format("friendly url not defined for category with id:%d", category.getId()));
  }

  private String generateFriendlyUrl(CategoryTranslateableAttributes attr) {
    String res = attr.getName().replaceAll("[^a-zA-Z]", "");
    return res.toLowerCase();
  }
}