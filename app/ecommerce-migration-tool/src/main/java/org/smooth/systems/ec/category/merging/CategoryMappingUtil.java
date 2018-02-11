package org.smooth.systems.ec.category.merging;

import org.smooth.systems.ec.migration.model.Category;

public final class CategoryMappingUtil {

  private CategoryMappingUtil() {
  }

  public static Category retrieveCategoryById(Category category, Long categoryId) {
    if (categoryId.equals(category.getId())) {
      return category;
    }
    for (Category subCategory : category.getChildrens()) {
      Category resCategory = retrieveCategoryById(subCategory, categoryId);
      if (resCategory != null) {
        return resCategory;
      }
    }
    return null;
  }
}
