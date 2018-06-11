package org.smooth.systems.ec.utils.alpha.concept;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.smooth.systems.ec.client.api.MigrationSystemReader;
import org.smooth.systems.ec.client.api.MigrationSystemWriter;
import org.smooth.systems.ec.client.util.ObjectStringToIdMapper;
import org.smooth.systems.ec.component.MigrationSystemReaderAndWriterFactory;
import org.smooth.systems.ec.configuration.MigrationConfiguration;
import org.smooth.systems.ec.migration.model.Manufacturer;
import org.smooth.systems.ec.utils.db.api.IActionExecuter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions> on
 * 09.06.18.
 */
@Slf4j
@Component
public class IlluminazioneALedBrandsCreatorExecutor implements IActionExecuter {

  private MigrationConfiguration config;

  private MigrationSystemReader reader;

  private MigrationSystemWriter writer;

  private ObjectStringToIdMapper brandsMapping;

  private List<Manufacturer> existingManufacturers = new ArrayList<>();

  private MigrationSystemReaderAndWriterFactory readerWriterFactory;

  private final static List<String> brands = Arrays.asList("PROLED", "MBNLED", "alpha concept", "JB-Ligthning", "Eurolite",
      "Mean Well");

  @Autowired
  public IlluminazioneALedBrandsCreatorExecutor(MigrationConfiguration config, MigrationSystemReaderAndWriterFactory readerWriterFactory) {
    this.config = config;
    this.readerWriterFactory = readerWriterFactory;
  }

  public void init() {
    writer = readerWriterFactory.getMigrationWriter();
    reader = readerWriterFactory.getMigrationReader(config.getDestinationSystemName());
    brandsMapping = new ObjectStringToIdMapper(config.getGeneratedProductsBrandMappingFile());
    try {
      existingManufacturers = reader.readAllManufacturers();      
    } catch(Exception e) {
      if(!(e instanceof RestClientException)) {
        throw new IllegalStateException(e);
      }
    }
  }

  @Override
  public void execute() {
    log.info("execute()");
    init();
    brands.forEach(brand -> {
      Long manufacturerId = createOrRetrieveManufacturer(brand);
      brandsMapping.addMapping(brand, manufacturerId);
    });
    brandsMapping.writeMappingToFile("Created brands on destination system");
  }

  @Override
  public String getActionName() {
    return "create-led-brands";
  }

  private Long createOrRetrieveManufacturer(String manufacturerName) {
    Optional<Manufacturer> manufacturerOptional = existingManufacturers.stream().filter(manu -> manu.getName().equals(manufacturerName))
        .findFirst();
    if(manufacturerOptional.isPresent()) {
      return manufacturerOptional.get().getId();
    }
    Manufacturer manufacturer = writer.writeManufacturer(manufacturerName);
    return manufacturer.getId();
  }
}
