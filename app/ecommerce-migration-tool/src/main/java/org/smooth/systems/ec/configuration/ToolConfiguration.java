package org.smooth.systems.ec.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ToolConfiguration {

  @Bean
  public MigrationConfiguration createConfiguration() {
    return new MigrationConfiguration();
  }
}
