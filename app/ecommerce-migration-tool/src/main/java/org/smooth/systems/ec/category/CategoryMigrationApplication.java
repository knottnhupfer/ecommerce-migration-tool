package org.smooth.systems.ec.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CategoryMigrationApplication {

  private ReadAndMergeController mergeController;

  private WriteAndStoreController writeController;

  @Autowired
  public CategoryMigrationApplication(ReadAndMergeController mergeController, WriteAndStoreController writeController) {
    this.mergeController = mergeController;
    this.writeController = writeController;
  }

  public void executeApplication() {
    mergeController.readCategoriesAndMerge();
//    writeController.writeCategoriesAndStoreMapping();
  }
}
