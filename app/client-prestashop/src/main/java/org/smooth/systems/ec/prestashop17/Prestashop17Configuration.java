package org.smooth.systems.ec.prestashop17;

import org.smooth.systems.ec.client.api.MigrationClientConstants;
import org.smooth.systems.ec.prestashop17.api.Prestashop17Constants;
import org.smooth.systems.ec.prestashop17.client.Prestashop17Client;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@ConditionalOnProperty(prefix = Prestashop17Constants.PRESTASHOP17_CONFIG_PREFIX, name = MigrationClientConstants.MIGRATION_CLIENT_BASE_URL)
public class Prestashop17Configuration {

  @Bean
  public Prestashop17Client createClient(Prestashop17ConnectorConfiguration config) {
    log.info("createClient({})", config);
    return new Prestashop17Client(config.getBaseUrl(), config.getAuthToken());
  }
}
