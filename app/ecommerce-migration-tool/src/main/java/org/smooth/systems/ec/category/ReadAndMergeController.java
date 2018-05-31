package org.smooth.systems.ec.category;

import java.util.ArrayList;
import java.util.List;

import org.smooth.systems.ec.category.merging.IDataModelCategoryMerger;
import org.smooth.systems.ec.client.api.MigrationSystemReader;
import org.smooth.systems.ec.client.api.SimpleCategory;
import org.smooth.systems.ec.component.MigrationSystemReaderAndWriterFactory;
import org.smooth.systems.ec.configuration.MigrationConfiguration;
import org.smooth.systems.ec.exceptions.NotFoundException;
import org.smooth.systems.ec.migration.model.Category;
import org.smooth.systems.ec.model.DataModelLogger;
import org.smooth.systems.ec.model.MigrationCategoryModel;
import org.smooth.systems.utils.ErrorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ReadAndMergeController {

  private final MigrationCategoryModel dataModel;

  private final MigrationConfiguration config;

  private final DataModelLogger dataModelLogger;

  private final IDataModelCategoryMerger modelMerger;

  private final MigrationSystemReaderAndWriterFactory factory;

  @Autowired
  public ReadAndMergeController(MigrationCategoryModel dataModel, MigrationSystemReaderAndWriterFactory factory,
      IDataModelCategoryMerger modelMerger, DataModelLogger dataModelLogger, MigrationConfiguration config) {
    this.config = config;
    this.factory = factory;
    this.dataModel = dataModel;
    this.modelMerger = modelMerger;
    this.dataModelLogger = dataModelLogger;
  }

  public void readCategoriesAndMerge() {
    List<Category> readCategories = readCategoriesAndCreate();
    log.info("Read successfully {} root categories", readCategories.size());
    dataModel.setReadCategories(readCategories);
    dataModelLogger.printReadCategories();

    modelMerger.mergeDataModel();
    dataModelLogger.printMergedCategories();

    dataModelLogger.printUnmergedCategories();
    ErrorUtil.throwAndLog(!dataModel.getUnmappedCategories().isEmpty(), "Not all categories have been mapped.");
  }

  private List<Category> readCategoriesAndCreate() {
    try {
      MigrationSystemReader reader = factory.getSystemReaderForType(config.getSourceSystemName());
      List<SimpleCategory> categoryReadConfigs = retrieveCategoryConfig(config);
      return reader.readAllCategories(categoryReadConfigs);
    } catch (NotFoundException e) {
      log.error("Error while reading categories. Reason: {}", e.getMessage());
      throw new IllegalStateException(e.getMessage());
    }
  }

  private List<SimpleCategory> retrieveCategoryConfig(MigrationConfiguration config) {
    List<SimpleCategory> res = new ArrayList<>();
    res.add(new SimpleCategory(config.getRootCategoryId(), config.getRootCategoryLanguage()));
    config.getAdditionalCategories().forEach(cat -> res.add(new SimpleCategory(cat.getCategoryId(), cat.getCategoryLanguage())));
    return res;
  }
}
