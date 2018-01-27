package org.smooth.systems.ec.magento;

import org.smooth.systems.ec.client.api.RegisterableComponent;
import org.springframework.util.Assert;

import com.github.chen0040.magento.MagentoClient;

public class AbstractMagentoConnector implements RegisterableComponent {

  public static final String MAGENTO2_CONNECTOR_NAME = "magento2";

  protected final Magento2ConnectorConfiguration config;

  public AbstractMagentoConnector(Magento2ConnectorConfiguration config) {
    this.config = config;
  }

  private MagentoClient client;

  @Override
  public String getName() {
    return MAGENTO2_CONNECTOR_NAME;
  }

  protected synchronized MagentoClient getClient() {
    if (client == null) {
      client = new MagentoClient(config.getBaseUrl());
      String token = client.loginAsAdmin(config.getUser(), config.getPassword());
      Assert.notNull(token, "Magento2 client token is null, client not connected ...");
    }
    return client;
  }
}