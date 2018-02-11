package org.smooth.systems.ec.model.logging;

import java.util.Collections;
import java.util.List;

import org.smooth.systems.ec.migration.model.Category;

public class ObjectLogger {

  public enum LoggingType {
    SIMPLE_LOGGING, EXTENDED_LOGGING
  }

  public static void printCategories(List<Category> categories) {
    printCategories(categories, 1, new SimpleObjectLogger());
  }

  public static void printExtendedCategories(Category rootCategory) {
    printCategories(Collections.singletonList(rootCategory), 1, new ExtendedObjectLogger());
  }

  private static void printCategories(List<Category> categories, int level, IObjectLogger logger) {
    for (Category category : categories) {
      printCategoryWithIndent(category, level, logger);
      for (Category subCategory : category.getChildrens()) {
        printCategories(Collections.singletonList(subCategory), level + 1, logger);
      }
    }
  }

  private static void printCategoryWithIndent(Category category, int level, IObjectLogger logger) {
    logger.printCategoryWithIndent(category, level);
  }
}
