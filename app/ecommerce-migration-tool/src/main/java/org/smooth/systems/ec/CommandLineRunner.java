package org.smooth.systems.ec;

import org.smooth.systems.ec.category.CategoryMigrationApplication;
import org.smooth.systems.ec.configuration.MigrationConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CommandLineRunner extends AbstractCommandLineRunner implements ApplicationRunner {

  public static String OPTION_CONFIG_NAME = "migration.configuration";

  public static String OPTION_MERGE_CATEGORY_NAME = "merge-category";

  private final CategoryMigrationApplication categoryMigrator;

  @Autowired
  public CommandLineRunner(CategoryMigrationApplication application) {
    this.categoryMigrator = application;
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    executeCommand(args);
  }

  private void executeCommand(ApplicationArguments args) {
    log.info("executeCommand(ApplicationArguments args)");
    if (isArgumentSet(args, OPTION_MERGE_CATEGORY_NAME)) {
      categoryMigrator.executeApplication();
      System.exit(0);
    } else if (isArgumentSet(args, OPTION_MERGE_CATEGORY_NAME)) {
      throw new IllegalStateException("Product migrator not implemented yet.");
    } else {
      log.error("");
      log.error("ERROR: No option chosen. Choose one of the following:");
      log.error("");
      log.error("    <program> --{} --{}=/path/to/config", OPTION_MERGE_CATEGORY_NAME, OPTION_CONFIG_NAME);
      log.error("");
//      log.error("    <program> --{}", OPTION_MERGE_PRODUCTS_NAME);
//      log.error("");
      System.exit(5);
    }
  }
}
