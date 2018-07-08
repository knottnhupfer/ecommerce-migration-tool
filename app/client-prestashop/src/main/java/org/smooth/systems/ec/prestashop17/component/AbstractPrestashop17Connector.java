package org.smooth.systems.ec.prestashop17.component;

import org.smooth.systems.ec.client.api.RegisterableComponent;
import org.smooth.systems.ec.configuration.MigrationConfiguration;
import org.smooth.systems.ec.prestashop17.api.Prestashop17Constants;
import org.smooth.systems.ec.prestashop17.client.Prestashop17Client;

public abstract class AbstractPrestashop17Connector implements RegisterableComponent {

  protected final Prestashop17Client client;

  protected final MigrationConfiguration config;

  protected AbstractPrestashop17Connector(MigrationConfiguration config, Prestashop17Client client) {
    this.client = client;
    this.config = config;
  }
  
  @Override
  public String getName() {
    return Prestashop17Constants.PRESTASHOP17_SYSTEM_NAME;
  }
}