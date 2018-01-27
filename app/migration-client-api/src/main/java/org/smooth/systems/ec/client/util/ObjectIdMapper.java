package org.smooth.systems.ec.client.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.smooth.systems.ec.configuration.MigrationConfiguration;
import org.smooth.systems.ec.exceptions.NotFoundException;
import org.smooth.systems.utils.ErrorUtil;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
public class ObjectIdMapper {

  private Map<Long, Long> categoryIdMapping = new HashMap<>();

  public Long getMappedIdForId(Long idToMap) throws NotFoundException {
    if (!categoryIdMapping.containsKey(idToMap)) {
      String msg = String.format("Unable to find resulting id for id:%d", idToMap);
      log.error(msg);
      throw new NotFoundException(msg);
    }
    return categoryIdMapping.get(idToMap);
  }

  public void addMapping(Long srcId, Long dstId) {
    if (categoryIdMapping.containsKey(srcId)) {
      String msg = String.format("Unable to add new mapping, id:{} already exists.", srcId);
      log.error(msg);
      throw new IllegalStateException(msg);
    }
    categoryIdMapping.put(srcId, dstId);
  }

  public void writeMappingToFile(String filePath) {
    Properties fileContent = new Properties();
    for (Long srcId : categoryIdMapping.keySet()) {
      fileContent.setProperty(srcId.toString(), categoryIdMapping.get(srcId).toString());
    }
    try {
      FileOutputStream out = new FileOutputStream(filePath);
      fileContent.store(out, "# defines the mapping from source category id to created category id in destination system");
      out.close();
    } catch (IOException e) {
      ErrorUtil.throwAndLog(String.format("Error while writing mapping file '%s'. Reason: %s", fileContent, e.getMessage()));
    }
  }

  public static ObjectIdMapper createCategoriesMapping(MigrationConfiguration config) {
    log.info("Initialize categories mapping with file: {}", config.getCategoriesMappingFile());
    ObjectIdMapper mapper = new ObjectIdMapper();

    Properties categoryMappingProperties = new Properties();
    try {
      FileInputStream is = new FileInputStream(config.getCategoriesMappingFile());
      categoryMappingProperties.load(is);
      Set<Object> keySet = categoryMappingProperties.keySet();
      for (Object key : keySet) {
        Long mappedCatId = Long.valueOf(((String) key).trim());
        Long dstCatId = Long.valueOf(((String) categoryMappingProperties.get(key)).trim());
        mapper.addMapping(mappedCatId, dstCatId);
      }
      is.close();
    } catch (IOException e) {
      ErrorUtil.throwAndLog(
          String.format("Error while reading mapping file '%s'. Reason: %s", config.getCategoriesMappingFile(), e.getMessage()));
    }
    log.info("Initialized object id mapper: {}", mapper.getClass().getCanonicalName());
    return mapper;
  }
}
