package org.smooth.systems.ec.magento;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import com.github.chen0040.magento.MagentoClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "migration.magento2", name = "base-url")
public class Magento2Configuration {

  @Bean
  public MagentoClient createMagento2Client(Magento2ConnectorConfiguration config) {
    log.info("createMagento2Client({})", config);
    MagentoClient client = new MagentoClient(config.getBaseUrl());
    String token = client.loginAsAdmin(config.getUser(), config.getPassword());
    Assert.notNull(token, "Magento2 client token is null, client not connected ...");
    return client;
  }
}
