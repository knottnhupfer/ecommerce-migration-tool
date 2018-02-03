package org.smooth.systems.ec.client.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.smooth.systems.ec.configuration.MigrationConfiguration;
import org.smooth.systems.ec.exceptions.NotFoundException;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smooth.systems.utils.FileUtils;

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
		FileUtils.writePropertiesToFile(fileContent, filePath, "# defines the mapping from source category id to created category id in destination system");
	}

	public static ObjectIdMapper createCategoriesMapping(MigrationConfiguration config) {
		log.info("Initialize categories mapping with file: {}", config.getCategoriesMappingFile());
		ObjectIdMapper mapper = new ObjectIdMapper();

		Properties categoryMappingProperties = FileUtils.readPropertiesFromFile(config.getCategoriesMappingFile());
		Set<Object> keySet = categoryMappingProperties.keySet();
		for (Object key : keySet) {
			Long mappedCatId = Long.valueOf(((String) key).trim());
			Long dstCatId = Long.valueOf(((String) categoryMappingProperties.get(key)).trim());
			mapper.addMapping(mappedCatId, dstCatId);
		}
		log.info("Initialized object id mapper: {}", mapper.getClass().getCanonicalName());
		return mapper;
	}
}
