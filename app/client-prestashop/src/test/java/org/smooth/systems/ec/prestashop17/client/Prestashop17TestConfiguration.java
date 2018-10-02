package org.smooth.systems.ec.prestashop17.client;

import org.smooth.systems.ec.prestashop17.Prestashop17ConnectorConfiguration;

public interface Prestashop17TestConfiguration {

  public static final String BASE_URL = "http://prestashop.local/api";
//  public static final String BASE_URL = "http://prestashop.monichi.com/api";
//  public static final String BASE_URL = "http://www.illuminazione-a-led.com/api";
  // public static final String BASE_URL =
  // "http://domain2-prestashop.local/api";

  public static final String AUTH_TOKEN = "WUIHXDKPX2WNUUBLAG6BLZ8FIAX6PM6P";

  public static Prestashop17Client createTestingClient() {
    Prestashop17Client client = new Prestashop17Client(BASE_URL, AUTH_TOKEN);
    client.getClient().getInterceptors().add(new ProductsCreationResponseFilterInterceptor());
    return client;
  }

  public static Prestashop17ConnectorConfiguration createTestingConfiguration() {
    Prestashop17ConnectorConfiguration config = new Prestashop17ConnectorConfiguration();
    config.setBaseUrl(BASE_URL);
    config.setAuthToken(AUTH_TOKEN);
    return config;
  }
}
