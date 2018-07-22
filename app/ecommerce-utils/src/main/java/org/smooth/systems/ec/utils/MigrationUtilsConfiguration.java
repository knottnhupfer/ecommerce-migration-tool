package org.smooth.systems.ec.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotNull;

@Data
@Configuration
@ConfigurationProperties
public class MigrationUtilsConfiguration {

  @NotNull
  private String action;
}
