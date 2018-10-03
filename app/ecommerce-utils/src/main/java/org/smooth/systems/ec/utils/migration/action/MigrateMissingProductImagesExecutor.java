package org.smooth.systems.ec.utils.migration.action;

import lombok.extern.slf4j.Slf4j;
import org.smooth.systems.ec.client.api.MigrationSystemReader;
import org.smooth.systems.ec.migration.model.IProductMetaData;
import org.smooth.systems.ec.utils.EcommerceUtilsActions;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions>
 */
@Slf4j
@Component
public class MigrateMissingProductImagesExecutor extends AbstractImagesMigrationExecutor {

	private List<Long> productIdsWithImages;

	@Override
	public void execute() {
		initialize();
		initializeProductCacheAndRetrieveList();
		log.info("Initialized readers and writer ...");

		List<IProductMetaData> productsWithMissingImages = filterDestinationProductsWithNoImages();
		log.info("Migrate images for {} products", productsWithMissingImages.size());

		productsWithMissingImages.forEach(product -> {
			uploadImageIfNotAlreadyUploaded(product.getSku());
		});
		log.info("Upload images for products with missing images ... DONE");
	}

	private List<IProductMetaData> filterDestinationProductsWithNoImages() {
		List<IProductMetaData> products = getProductsMetaDataFromDestinationSystem();
		return products.stream().filter(this::hasProductNoUploadedImages).collect(Collectors.toList());
	}

	@Override
	public String getActionName() {
		return EcommerceUtilsActions.PRODUCTS_IMAGE_MIGRATION_MISSING;
	}
}
