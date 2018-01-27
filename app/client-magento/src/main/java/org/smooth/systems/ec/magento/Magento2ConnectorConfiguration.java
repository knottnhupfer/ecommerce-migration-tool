package org.smooth.systems.ec.magento;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "migration.magento2")
@ConditionalOnProperty(prefix = "migration.magento2", name = "base-url")
public class Magento2ConnectorConfiguration {

  private String baseUrl;

  private String user;

  private String password;

  private List<Long> rootCategories;
}