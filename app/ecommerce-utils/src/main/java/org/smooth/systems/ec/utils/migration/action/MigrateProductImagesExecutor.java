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
public class MigrateProductImagesExecutor extends AbstractProductsMigrationExecuter {

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

		List<MigrationProductImagesObject> imagesInfo = generateProductImagesObjects(mainProducts);
		log.info("Generated information objects to start images upload for main products.");

		uploadProductImages(imagesInfo);
	}

	private void uploadProductImages(List<MigrationProductImagesObject> imagesInfo) {
		MigrationSystemWriter writer = readerWriterFactory.getMigrationWriter();
		for (MigrationProductImagesObject imageObject : imagesInfo) {
			log.info("Upload product images: {}", imageObject);
			for (File imageUrl : imageObject.getImageUrls()) {
				writer.uploadProductImages(imageObject.getDstProductId(), imageUrl);
			}
		}
	}

	private List<MigrationProductImagesObject> generateProductImagesObjects(List<ProductId> products) {
		File imagesUrl = new File(config.getProductsImagesDirectory());
		List<MigrationProductImagesObject> imagesObjects = new ArrayList<>();

		for(ProductId productIdSrcSystem : products) {
			Product product = productsCache.getProductById(productIdSrcSystem.getProductId());
			if(!doesProductWithSkuExists(product.getSku()) || hasProductAlreadyAppendedImages(product)) {
				log.warn("Skip product images for product with sku '{}', does not exists on the destination system.", product.getSku());
				continue;
			}

			Long dstProductId = getProductIdDestinationSystemForProductSku(product.getSku());
			List<String> imageUrls = product.getProductImageUrls();
			List<File> absoluteImagesPaths = imageUrls.stream().map(url -> new File(imagesUrl, url)).collect(Collectors.toList());
			MigrationProductImagesObject imagesObj = new MigrationProductImagesObject(productIdSrcSystem.getProductId(), absoluteImagesPaths);

			imagesObj.setDstProductId(dstProductId);
			imagesObjects.add(imagesObj);
		}
		return imagesObjects;
	}

	private boolean hasProductAlreadyAppendedImages(Product prod) {
		// TODO check existing images and print if already uploaded
		return false;
	}
}