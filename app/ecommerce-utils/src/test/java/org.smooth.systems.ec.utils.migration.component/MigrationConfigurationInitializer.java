package org.smooth.systems.ec.utils.migration.component;

import lombok.extern.slf4j.Slf4j;
import org.smooth.systems.ec.configuration.MigrationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.io.File;

@Slf4j
@Configuration
public class MigrationConfigurationInitializer {

  @Resource
  private File configurationFile;

  @Bean
  public MigrationConfiguration createDefaultConfiguration() {
    log.info("Read from file: {}", configurationFile.getAbsolutePath());
    MigrationConfiguration config = new MigrationConfiguration();
    return config;
  }
}
