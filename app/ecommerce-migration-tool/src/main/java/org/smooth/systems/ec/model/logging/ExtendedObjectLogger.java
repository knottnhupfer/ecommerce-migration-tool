package org.smooth.systems.ec.model.logging;

import org.apache.commons.lang3.StringUtils;
import org.smooth.systems.ec.migration.model.Category;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExtendedObjectLogger implements IObjectLogger {

  @Override
  public void printCategoryWithIndent(Category category, int level) {
    String indent = StringUtils.repeat("  ", level);
    log.info(indent + category.extendedDescription());
  }
}
