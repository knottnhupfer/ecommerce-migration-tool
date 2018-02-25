package org.smooth.systems.ec.client.util;

import lombok.extern.slf4j.Slf4j;
import org.smooth.systems.ec.exceptions.NotFoundException;
import org.smooth.systems.utils.FileUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

@Slf4j
public abstract class ObjectMapper<K, V> {

  private final File file;

  private V defaultValue;

  private Map<K, V> categoryIdMapping = new HashMap<>();

  public ObjectMapper(String fileName) {
    this(fileName, null);
  }

  public ObjectMapper(String fileName, V defaultValue) {
    this.file = new File(fileName);
    this.defaultValue = defaultValue;
  }

  public V getMappedIdForId(K idToMap) throws NotFoundException {
    if (!categoryIdMapping.containsKey(idToMap)) {
      if (defaultValue != null) {
        log.debug("Use default value: {} for key: {}", defaultValue, idToMap);
        return defaultValue;
      }
      String msg = String.format("Unable to find resulting id for id:%d", idToMap);
      log.error(msg);
      throw new NotFoundException(msg);
    }
    return categoryIdMapping.get(idToMap);
  }

  public V getMappedIdForIdOrDefault(K idToMap) {
    if (!categoryIdMapping.containsKey(idToMap)) {
      if (defaultValue == null) {
        String msg = String.format("Unable to find resulting id for id:%d", idToMap);
        log.error(msg);
        throw new IllegalStateException(msg);        
      }
      log.debug("Use default value: {} for key: {}", defaultValue, idToMap);
      return defaultValue;
    }
    return categoryIdMapping.get(idToMap);
  }

  public void addMapping(K srcId, V dstId) {
    if (categoryIdMapping.containsKey(srcId)) {
      String msg = String.format("Unable to add new mapping, id:{} already exists.", srcId);
      log.error(msg);
      throw new IllegalStateException(msg);
    }
    categoryIdMapping.put(srcId, dstId);
  }

  public void writeMappingToFile(String comment) {
    Properties fileContent = new Properties();
    for (K srcId : categoryIdMapping.keySet()) {
      fileContent.setProperty(srcId.toString(), categoryIdMapping.get(srcId).toString());
    }
    FileUtils.writePropertiesToFile(fileContent, file.getAbsolutePath(), comment);
  }

  public void initializeIdMapperFromFile() {
    log.info("Initialize categories mapping with file: {}", file.getAbsolutePath());
    Properties categoryMappingProperties = FileUtils.readPropertiesFromFile(file.getAbsolutePath());
    Set<Object> keySet = categoryMappingProperties.keySet();
    for (Object key : keySet) {
      K id = convertStringToKey((String) key);
      V value = convertStringToValue((String) categoryMappingProperties.get(key));
      addMapping(id, value);
    }
    log.info("Initialized object id mapper from file: {}", file.getAbsolutePath());
  }

  public Set<K> keySet() {
    return categoryIdMapping.keySet();
  }

  public V getDefaultValue() {
    return defaultValue;
  }

  protected abstract K convertStringToKey(String key);

  protected abstract V convertStringToValue(String key);
}
