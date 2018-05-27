package org.smooth.systems.ec.prestashop17.component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.smooth.systems.ec.client.api.MigrationClientConstants;
import org.smooth.systems.ec.prestashop17.api.Prestashop17Constants;
import org.smooth.systems.ec.prestashop17.client.Prestashop17Client;
import org.smooth.systems.ec.prestashop17.model.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@ConditionalOnProperty(prefix = Prestashop17Constants.PRESTASHOP17_CONFIG_PREFIX, name = MigrationClientConstants.MIGRATION_CLIENT_BASE_URL)
public class PrestashopLanguageTranslatorCache {

  private final Prestashop17Client client;

  private final Map<String, Long> codeToIdMapping = new HashMap<>();

  private final Map<Long, String> idToCodeMapping = new HashMap<>();

  @Autowired
  public PrestashopLanguageTranslatorCache(Prestashop17Client client) {
    this.client = client;
  }

  @PostConstruct
  public void initialize() {
    List<Language> languages = client.getLanguages();
    for (Language language : languages) {
      String code = language.getIsoCode().substring(0, 2);
      codeToIdMapping.put(code, language.getId());
      idToCodeMapping.put(language.getId(), code);
    }
  }

  public Long getLangId(String code) {
    Long id = codeToIdMapping.get(code);
    Assert.notNull(id,
        String.format("Unable to map langCode '%s' to prestashop id. Available codes are: %s", code, codeToIdMapping.keySet()));
    return id;
  }

  public String getLangCode(Long id) {
    String code = idToCodeMapping.get(id);
    Assert.notNull(code,
        String.format("Unable to map prestashop langId '%d' to code. Available ids are: %s", id, idToCodeMapping.keySet()));
    return code;
  }
}