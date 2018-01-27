package org.smooth.systems.ec.prestashop17;

import org.smooth.systems.ec.client.api.MigrationClientConstants;
import org.smooth.systems.ec.prestashop17.api.Prestashop17Constants;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = Prestashop17Constants.PRESTASHOP17_CONFIG_PREFIX)
@ConditionalOnProperty(prefix = Prestashop17Constants.PRESTASHOP17_CONFIG_PREFIX, name = MigrationClientConstants.MIGRATION_CLIENT_BASE_URL)
public class Prestashop17ConnectorConfiguration {

  private String baseUrl;

  private String authToken;

  private Long baseCategoryId;
}