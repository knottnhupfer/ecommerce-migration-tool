package org.smooth.systems.ec.magento19.db.dummy;

import java.io.File;

import org.smooth.systems.ec.configuration.MigrationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DummyClientMagento19DbConfiguration {

  @Bean
  @Primary
  public MigrationConfiguration createDummyMigrationConfiguration() {
    MigrationConfiguration config = new MigrationConfiguration();
    config.setProductsBrandMappingFile(getPwd() + "src/test/resources/config/products-brands-mapping.properties");
    return config;
  }

  private String getPwd() {
    File file = new java.io.File(".");
    String path = file.getAbsolutePath();
    path = path.substring(0, path.length() - 1);
    System.out.println("Path: " + path);
    return path;
  }
}