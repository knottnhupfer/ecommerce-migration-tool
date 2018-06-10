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

  public static String OPTION_CONFIG_NAME = "config";

  public static String OPTION_MERGE_CATEGORY_NAME = "merge-category";

//  public static String OPTION_MERGE_PRODUCTS_NAME = "merge-products";

  private final MigrationConfiguration config;

  private final CategoryMigrationApplication categoryMigrator;

  @Autowired
  public CommandLineRunner(MigrationConfiguration config, CategoryMigrationApplication application) {
    this.config = config;
    this.categoryMigrator = application;
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    MigrationConfiguration migrationConfiguration = getMigrationConfiguration(args, OPTION_CONFIG_NAME);
    config.storeConfiguration(migrationConfiguration);
    executeCommand(args);
  }

  private void executeCommand(ApplicationArguments args) {
    log.info("executeCommand(ApplicationArguments args)");
    if (isArgumentSet(args, OPTION_MERGE_CATEGORY_NAME)) {
      categoryMigrator.executeApplication();
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
