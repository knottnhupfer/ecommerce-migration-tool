package org.smooth.systems.ec.model.logging;

import org.smooth.systems.ec.migration.model.Category;

public interface IObjectLogger {

  void printCategoryWithIndent(Category category, int level);
}
