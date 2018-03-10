package org.smooth.systems.ec.client.util;

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
}
