package org.smooth.systems.ec.category;

import org.smooth.systems.ec.client.api.MigrationSystemWriter;
import org.smooth.systems.ec.component.MigrationSystemReaderAndWriterFactory;
import org.smooth.systems.ec.configuration.MigrationConfiguration;
import org.smooth.systems.ec.exceptions.NotFoundException;
import org.smooth.systems.ec.model.MigrationCategoryModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CategoryWriterController {

  private final MigrationConfiguration config;

  private final MigrationSystemReaderAndWriterFactory factory;

  @Autowired
  public CategoryWriterController(MigrationSystemReaderAndWriterFactory factory, MigrationCategoryModel dataModel,
      MigrationConfiguration config) {
    this.config = config;
    this.factory = factory;
  }

  public void writeCategories() {
    try {
      MigrationSystemWriter systemWriterForType = factory.getSystemWriterForType(config.getDestinationSystemName());
    } catch (NotFoundException e) {
      e.printStackTrace();
    }
  }
}
