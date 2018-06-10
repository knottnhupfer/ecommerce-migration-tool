package org.smooth.systems.ec.client.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.io.File;

public class ObjectStringToIdMapper extends ObjectMapper<String, Long> {

  public ObjectStringToIdMapper(String fileName) {
    super(fileName);
  }

  public ObjectStringToIdMapper(String fileName, Long defaultValue) {
    super(fileName, defaultValue);
  }

  @Override
  protected String convertStringToKey(String key) {
    return key.trim();
  }

  @Override
  protected Long convertStringToValue(String value) {
    return Long.valueOf((value).trim());
  }

  public static ObjectStringToIdMapper loadFile(File file) {
    Assert.isTrue(file.isFile(), String.format("%s is not a valid file", file.getAbsolutePath()));
    ObjectStringToIdMapper idMapper = new ObjectStringToIdMapper(file.getAbsolutePath());
    idMapper.initializeIdMapperFromFile();
    return idMapper;
  }
}
