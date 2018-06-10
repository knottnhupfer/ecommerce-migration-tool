package org.smooth.systems.ec.utils.alpha.concept;

import org.smooth.systems.ec.client.api.MigrationSystemWriter;
import org.smooth.systems.ec.client.util.ObjectIdMapper;
import org.smooth.systems.ec.client.util.ObjectStringToIdMapper;
import org.smooth.systems.ec.component.MigrationSystemReaderAndWriterFactory;
import org.smooth.systems.ec.configuration.MigrationConfiguration;
import org.smooth.systems.ec.utils.db.api.IActionExecuter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions> on 09.06.18.
 */
@Component
public class IlluminazioneALedBrandsCreatorExecutor implements IActionExecuter {

  private MigrationSystemWriter writer;

  private ObjectStringToIdMapper brandsMapping;

  private final static List<String> brands = Arrays.asList("PROLED", "MBNLED", "alpha\\u0020concept", "JB-Ligthning", "Eurolite", "Mean\\u0020Well");

  @Autowired
  public IlluminazioneALedBrandsCreatorExecutor(MigrationConfiguration config, ObjectIdMapper brandsMapper, MigrationSystemReaderAndWriterFactory readerWriterFactory) {
    writer = readerWriterFactory.getMigrationWriter();
    brandsMapping = new ObjectStringToIdMapper(config.getProductsBrandMappingFile());
  }

  @Override
  public void execute() {
    // TODO remove exiting brands???
    brands.forEach(brand -> {
      Long brandId = writer.writeBrand(brand);
      brandsMapping.addMapping(brand, brandId);
    });
    brandsMapping.writeMappingToFile("Created brands on destination system");
  }

  @Override
  public String getActionName() {
    return "create-led-brands";
  }
}
