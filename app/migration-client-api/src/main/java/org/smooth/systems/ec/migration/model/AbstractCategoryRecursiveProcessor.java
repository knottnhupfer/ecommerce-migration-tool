package org.smooth.systems.ec.migration.model;

import java.util.Collections;
import java.util.List;

public abstract class AbstractCategoryRecursiveProcessor {

  public void executeRecursive(Category rootCategory) {
    executeCategories(Collections.singletonList(rootCategory), 1);
  }

  private void executeCategories(List<Category> categories, int level) {
    for (Category category : categories) {
      executeCategory(category, level);
      for (Category subCategory : category.getSubCategories()) {
        executeCategories(Collections.singletonList(subCategory), level + 1);
      }
    }
  }

  protected abstract void executeCategory(Category category, int level);
}