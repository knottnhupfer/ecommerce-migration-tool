package org.smooth.systems.ec.utils.migration.action;

import java.util.List;

import org.smooth.systems.ec.client.api.ObjectId;
import org.smooth.systems.ec.migration.model.Product;
import org.smooth.systems.ec.utils.EcommerceUtilsActions;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions>
 */
@Slf4j
@Component
public class MigrateProductImagesExecutor extends AbstractImagesMigrationExecutor {

	public MigrateProductImagesExecutor() {
		super(EcommerceUtilsActions.PRODUCTS_IMAGE_MIGRATION);
	}

	@Override
	public void execute() {
		initialize();
		log.trace("execute()");

		List<ObjectId> mainProducts = initializeSourceSystemProductCacheAndRetrieveList();
		log.info("Read products and initialized cache");

		uploadProductImages(mainProducts);
		log.info("Uploaded all images for {} products.", mainProducts.size());
	}

	private void uploadProductImages(List<ObjectId> products) {
		log.info("uploadProductImages({})", products.size());
		for(ObjectId productIdSrcSystem : products) {
			Product product = srcProductsCache.getProductById(productIdSrcSystem.getObjectId());

			if(!doesProductOnDestinationSystemWithSkuExists(product.getSku())) {
				log.warn("Skip product images for product with sku '{}', does not exists on the destination system.", product.getSku());
				continue;
			}

			uploadImageIfNotAlreadyUploaded(product.getSku());
		}
	}
}