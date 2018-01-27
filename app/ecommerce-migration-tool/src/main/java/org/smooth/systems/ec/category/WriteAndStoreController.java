package org.smooth.systems.ec.category;

import org.smooth.systems.ec.client.api.MigrationSystemWriter;
import org.smooth.systems.ec.client.util.ObjectIdMapper;
import org.smooth.systems.ec.component.MigrationSystemReaderAndWriterFactory;
import org.smooth.systems.ec.configuration.MigrationConfiguration;
import org.smooth.systems.ec.exceptions.NotFoundException;
import org.smooth.systems.ec.exceptions.ObjectAlreadyExistsException;
import org.smooth.systems.ec.model.DataModelLogger;
import org.smooth.systems.ec.model.MigrationCategoryModel;
import org.smooth.systems.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class WriteAndStoreController {

  private final MigrationConfiguration config;

  private final DataModelLogger dataModelLogger;

  private final MigrationCategoryModel dataModel;

  private final MigrationSystemReaderAndWriterFactory factory;

  private MigrationSystemWriter systemWriter;

  @Autowired
  public WriteAndStoreController(MigrationCategoryModel dataModel, MigrationSystemReaderAndWriterFactory factory,
      DataModelLogger dataModelLogger, MigrationConfiguration config) {
    this.config = config;
    this.factory = factory;
    this.dataModel = dataModel;
    this.dataModelLogger = dataModelLogger;
  }

  public void writeCategoriesAndStoreMapping() {
    log.info("writeCategoriesAndStoreMapping()");
    initialize();
    try {
//      systemWriter.prepareDataModel(dataModel.getMergeCategories());
      systemWriter.writeCategories(dataModel.getMergeCategories(), true);
      ObjectIdMapper categoryIdMapper = systemWriter.getCategoriesObjectIdMapper();
      categoryIdMapper.writeMappingToFile(FileUtils.getConfigPathName("created_category_mapping.properties"));
      dataModelLogger.printIgnoredCategories();
    } catch (ObjectAlreadyExistsException e) {
      String msg = String.format("Unable to write categories to destination system. Reason: %s", e.getMessage());
      log.error(msg);
      throw new IllegalStateException(msg);
    }
  }

  private void initialize() {
    log.info("initialize()");
    try {
      systemWriter = factory.getSystemWriterForType(config.getDestinationSystemName());
    } catch (NotFoundException e) {
      String msg = String.format("Unable to retrieve writer for name '%s'", config.getDestinationSystemName());
      log.error(msg);
      throw new IllegalStateException(msg);
    }
  }
}