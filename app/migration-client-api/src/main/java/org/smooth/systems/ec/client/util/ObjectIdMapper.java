package org.smooth.systems.ec.client.util;

import java.io.File;
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
public class ObjectIdMapper {

	private final File file;

	private final String comment;

	private Map<Long, Long> categoryIdMapping = new HashMap<>();

	public ObjectIdMapper(String fileName, String comment) {
		this.file = new File(fileName);
		this.comment = comment;
	}

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

	public void writeMappingToFile() {
		Properties fileContent = new Properties();
		for (Long srcId : categoryIdMapping.keySet()) {
			fileContent.setProperty(srcId.toString(), categoryIdMapping.get(srcId).toString());
		}
		FileUtils.writePropertiesToFile(fileContent, file.getAbsolutePath(), comment);
	}

	public void initializeIdMapperFromFile() {
		log.info("Initialize categories mapping with file: {}", file.getAbsolutePath());
		Properties categoryMappingProperties = FileUtils.readPropertiesFromFile(file.getAbsolutePath());
		Set<Object> keySet = categoryMappingProperties.keySet();
		for (Object key : keySet) {
			Long mappedCatId = Long.valueOf(((String) key).trim());
			Long dstCatId = Long.valueOf(((String) categoryMappingProperties.get(key)).trim());
			addMapping(mappedCatId, dstCatId);
		}
		log.info("Initialized object id mapper from file: {}", file.getAbsolutePath());
	}
}
