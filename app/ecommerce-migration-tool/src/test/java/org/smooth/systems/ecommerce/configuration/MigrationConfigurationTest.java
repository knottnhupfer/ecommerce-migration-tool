package org.smooth.systems.ecommerce.configuration;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.smooth.systems.ec.configuration.MigrationConfiguration;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class MigrationConfigurationTest {

  @Test
  public void readSimpleConfiguration() throws JsonParseException, JsonMappingException, IOException {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    MigrationConfiguration config = mapper.readValue(new File("src/main/resources/config/migration-config.yaml"), MigrationConfiguration.class);
    System.out.println(config);    
  }
}
