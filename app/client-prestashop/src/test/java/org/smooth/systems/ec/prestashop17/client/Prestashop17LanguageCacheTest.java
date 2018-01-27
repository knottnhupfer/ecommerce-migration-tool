package org.smooth.systems.ec.prestashop17.client;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.smooth.systems.ec.prestashop17.component.PrestashopLanguageTranslatorCache;

public class Prestashop17LanguageCacheTest {

  @Test
  public void filleLanguageCacheTest() {
    PrestashopLanguageTranslatorCache cache = new PrestashopLanguageTranslatorCache(Prestashop17TestConfiguration.createTestingClient());
    cache.initialize();
    assertEquals(new Long(1), cache.getLangId("en"));
    assertEquals(new Long(2), cache.getLangId("de"));
  }
}