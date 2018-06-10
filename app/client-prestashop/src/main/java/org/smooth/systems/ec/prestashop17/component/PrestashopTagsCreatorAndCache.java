package org.smooth.systems.ec.prestashop17.component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.smooth.systems.ec.client.api.MigrationClientConstants;
import org.smooth.systems.ec.prestashop17.api.Prestashop17Constants;
import org.smooth.systems.ec.prestashop17.client.Prestashop17Client;
import org.smooth.systems.ec.prestashop17.model.Tag;
import org.smooth.systems.utils.ErrorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@ConditionalOnProperty(prefix = Prestashop17Constants.PRESTASHOP17_CONFIG_PREFIX, name = MigrationClientConstants.MIGRATION_CLIENT_BASE_URL)
public class PrestashopTagsCreatorAndCache {

  private final Prestashop17Client client;

  private PrestashopLanguageTranslatorCache langCache;

  private final Map<String, Long> tagToIdMapping = new HashMap<>();

  @Autowired
  public PrestashopTagsCreatorAndCache(Prestashop17Client client, PrestashopLanguageTranslatorCache langCache) {
    this.client = client;
    this.langCache = langCache;
  }

  @PostConstruct
  public void initialize() {
    initializeTagsMapping();
  }

  public Long getTagId(String langCode, String tagName) {
    String key = getUniqueIdentifier(langCode, tagName);
    if (tagToIdMapping.containsKey(key)) {
      return tagToIdMapping.get(key);
    }
    return ErrorUtil.throwAndLog(String.format("Unable to map tagName '%s' with langCode '%s' to prestashop id.", tagName, langCode));
  }

  private void initializeTagsMapping() {
    log.info("initializeTagsMapping()");
    try {
      List<Tag> tags = client.getTags();      
      tags.forEach(tag -> {
        String langCode = langCache.getLangCode(tag.getIdLang());
        String key = getUniqueIdentifier(langCode, tag.getName());
        tagToIdMapping.put(key, tag.getId());
      });
    } catch(Exception e) {
      if(!(e instanceof RestClientException)) {
        throw new IllegalStateException(e);
      }
      log.error("Unable to read tags. Reason: {}", e.getMessage());
    }
  }

  private String getUniqueIdentifier(String langCode, String tagName) {
    return String.format("%s_%s", langCode, tagName);
  }
}