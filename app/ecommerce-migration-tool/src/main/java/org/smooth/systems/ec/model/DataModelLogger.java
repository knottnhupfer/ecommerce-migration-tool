package org.smooth.systems.ec.model;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.smooth.systems.ec.migration.model.Category;
import org.smooth.systems.ec.model.logging.ExtendedObjectLogger;
import org.smooth.systems.ec.model.logging.IObjectLogger;
import org.smooth.systems.ec.model.logging.ObjectLogger;
import org.smooth.systems.ec.model.logging.SimpleObjectLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DataModelLogger {

  private String HORIZONTAL_DELIMITER = StringUtils.repeat("=", 90);

  private final MigrationCategoryModel migrationDataModel;

  @Autowired
  public DataModelLogger(MigrationCategoryModel migrationDataModel) {
    this.migrationDataModel = migrationDataModel;
  }

  public void printFullDataModel() {

    printReadCategories();

    printMergedCategories();

    printUnmergedCategories();
  }

  public void printReadCategories() {
    printSectionHeader(String.format("Read categories (%d):", migrationDataModel.readCategories.size()));
    ObjectLogger.printCategories(migrationDataModel.readCategories);
    log.info("");
    log.info("Read {} categories ...", migrationDataModel.readCategories.size());
    log.info("");
    printSectionFooter();
    
  }

  public void printMergedCategories() {
    printSectionHeader("Merged data model:");
    if (migrationDataModel.mergeCategories == null) {
      log.info("No merged categories");
    } else {
      ObjectLogger.printExtendedCategories(migrationDataModel.getMergeCategories());
    }
    printSectionFooter();
  }

  public void printUnmergedCategories() {
    printSectionHeader("Unmapped categories:");
    ObjectLogger.printCategories(migrationDataModel.getUnmappedCategories());
    printSectionFooter();
  }

  public void printIgnoredCategories() {
    printSectionHeader("Ignoring categories:");
    ObjectLogger.printCategories(migrationDataModel.getIgnoringCategories());
    printSectionFooter();
  }

  public void printSkippedCategories() {
    printSectionHeader("Skipped categories:");
    ObjectLogger.printCategories(migrationDataModel.getSkippedCategories());
    printSectionFooter();
  }

  private void printSectionHeader(String headerName) {
    log.info("");
    log.info(HORIZONTAL_DELIMITER);
    log.info(headerName);
    log.info("----");
  }

  private void printSectionFooter() {
    log.info(HORIZONTAL_DELIMITER);
    log.info("");
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
