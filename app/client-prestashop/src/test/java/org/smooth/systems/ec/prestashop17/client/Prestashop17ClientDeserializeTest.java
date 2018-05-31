package org.smooth.systems.ec.prestashop17.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.smooth.systems.ec.prestashop17.model.Language;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Prestashop17ClientDeserializeTest {

  @Test
  public void deserializeLanguagesResponse() throws JsonParseException, JsonMappingException, IOException {
    XmlMapper xmlMapper = new XmlMapper();
    File inputFile = new File("src/test/resources/example_responses/languages_response_prestashop_1-7-3.xml");
    Languages languages = xmlMapper.readValue(inputFile, Languages.class);
    log.info("Languages: {}", languages);
  }

  @Test
  public void deserializeLanguageResponse() throws JsonParseException, JsonMappingException, IOException {
    XmlMapper xmlMapper = new XmlMapper();
    File inputFile = new File("src/test/resources/example_responses/language_response_prestashop_1-7-3.xml");
    LanguageWrapper language = xmlMapper.readValue(inputFile, LanguageWrapper.class);
    log.info("LanguageWrapper: {}", language);

    assertNotNull(language.getLanguage());
    Language lang = language.getLanguage();
    assertEquals(new Long(1), lang.getId());
    assertEquals("English (English)", lang.getName());
    assertEquals("en", lang.getIsoCode());
  }

  @Test
  public void deserializeTagResponse() throws JsonParseException, JsonMappingException, IOException {
    XmlMapper xmlMapper = new XmlMapper();
    File inputFile = new File("src/test/resources/example_responses/tag_response_prestashop_1-7-3.xml");
    TagWrapper tag = xmlMapper.readValue(inputFile, TagWrapper.class);
    log.info("Languages: {}", tag);
    assertNotNull(tag.getTag());
    assertEquals(new Long(56), tag.getTag().getId());
    assertEquals(new Long(57), tag.getTag().getIdLang());
    assertEquals("test134", tag.getTag().getName());
  }

  @Test
  public void deserializeTagsResponse() throws JsonParseException, JsonMappingException, IOException {
    XmlMapper xmlMapper = new XmlMapper();
    File inputFile = new File("src/test/resources/example_responses/tags_response_prestashop_1-7-3.xml");
    Tags tags = xmlMapper.readValue(inputFile, Tags.class);
    log.info("Languages: {}", tags);
     assertNotNull(tags.getTags());
     assertEquals(2, tags.getTags().size());
     assertEquals(new Long(1), tags.getTags().get(0).getId());
     assertEquals(new Long(2), tags.getTags().get(1).getId());
  }
}