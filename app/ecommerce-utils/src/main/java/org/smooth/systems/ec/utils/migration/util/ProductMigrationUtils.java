package org.smooth.systems.ec.utils.migration.util;

import org.smooth.systems.ec.client.util.ObjectIdMapper;
import org.smooth.systems.ec.configuration.MigrationConfiguration;
import org.smooth.systems.ec.exceptions.NotFoundException;
import org.smooth.systems.ec.migration.model.Product;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public final class ProductMigrationUtils {

	private ProductMigrationUtils() {
	}

	public static void updateCategoryIdWithDestinationSystemCategoryId(MigrationConfiguration config, List<Product> mergedProducts) {
		ObjectIdMapper idMapper = new ObjectIdMapper(config.getGeneratedCreatedCategoriesMappingFile());
		idMapper.initializeIdMapperFromFile();
		for(Product prod : mergedProducts) {
			List<Long> dstCategoryIds = prod.getCategories().stream().map(
				catId -> {
				  try {				    
				    return mapCategoryId(idMapper, catId);
				  } catch(Exception e) {
				    log.error("Unable to map category for product: {}", prod.simpleDescription());
				    throw e;
				  }
				}
			).collect(Collectors.toList());
			prod.setCategories(dstCategoryIds);
		}
	}

	private static Long mapCategoryId(ObjectIdMapper idMapper, Long srcCatId) {
		try {
			return idMapper.getMappedIdForId(srcCatId);
		} catch (NotFoundException e) {
			throw new IllegalStateException();
		}
	}
}
