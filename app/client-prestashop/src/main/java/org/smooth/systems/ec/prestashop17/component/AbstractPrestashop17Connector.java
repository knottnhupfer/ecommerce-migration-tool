package org.smooth.systems.ec.prestashop17.component;

import org.smooth.systems.ec.client.api.RegisterableComponent;
import org.smooth.systems.ec.prestashop17.api.Prestashop17Constants;

public class AbstractPrestashop17Connector implements RegisterableComponent {

  @Override
  public String getName() {
    return Prestashop17Constants.PRESTASHOP17_SYSTEM_NAME;
  }
}