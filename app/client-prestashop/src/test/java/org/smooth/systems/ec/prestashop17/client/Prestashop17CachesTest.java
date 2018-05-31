package org.smooth.systems.ec.prestashop17.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.smooth.systems.ec.prestashop17.component.PrestashopLanguageTranslatorCache;
import org.smooth.systems.ec.prestashop17.component.PrestashopTagsCreatorAndCache;

public class Prestashop17CachesTest {

  private static Prestashop17Client client_ = Prestashop17TestConfiguration.createTestingClient();

  private static PrestashopLanguageTranslatorCache langCache_ = new PrestashopLanguageTranslatorCache(client_);

  @Test
  public void fillLanguageCacheTest() {
    langCache_.initialize();
    assertEquals(new Long(1), langCache_.getLangId("en"));
    assertEquals(new Long(2), langCache_.getLangId("de"));
  }

  @Test
  public void fillTagsCacheTest() {
    langCache_.initialize();
    PrestashopTagsCreatorAndCache tagsCache = new PrestashopTagsCreatorAndCache(client_, langCache_);
    tagsCache.initialize();
    Long tagId = tagsCache.getTagId("en", "test1");
    assertNotNull(tagId);
  }
}