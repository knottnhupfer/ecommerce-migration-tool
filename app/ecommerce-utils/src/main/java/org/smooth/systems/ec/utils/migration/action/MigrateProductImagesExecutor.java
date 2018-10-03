package org.smooth.systems.ec.utils.migration.action;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.smooth.systems.ec.client.api.MigrationSystemWriter;
import org.smooth.systems.ec.client.api.ProductId;
import org.smooth.systems.ec.exceptions.NotFoundException;
import org.smooth.systems.ec.migration.model.Product;
import org.smooth.systems.ec.utils.EcommerceUtilsActions;
import org.smooth.systems.ec.utils.migration.component.ProductsCache;
import org.smooth.systems.ec.utils.migration.model.MigrationProductImagesObject;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions>
 */
@Slf4j
@Component
public class MigrateProductImagesExecutor extends AbstractImagesMigrationExecutor {

	@Override
	public String getActionName() {
		return EcommerceUtilsActions.PRODUCTS_IMAGE_MIGRATION;
	}

	@Override
	public void execute() {
		initialize();
		log.trace("execute()");

		List<ProductId> mainProducts = initializeProductCacheAndRetrieveList();
		log.info("Read products and initialized cache");

		uploadProductImages(mainProducts);
		log.info("Uploaded all images for {} products.", mainProducts.size());
	}

	private void uploadProductImages(List<ProductId> products) {
		log.info("uploadProductImages({})", products.size());
		for(ProductId productIdSrcSystem : products) {
			Product product = productsCache.getProductById(productIdSrcSystem.getProductId());

			if(!doesProductWithSkuExists(product.getSku())) {
				log.warn("Skip product images for product with sku '{}', does not exists on the destination system.", product.getSku());
				continue;
			}

			uploadImageIfNotAlreadyUploaded(product.getSku());
		}
	}
}