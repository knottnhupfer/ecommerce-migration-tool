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
 * Created by David Monichi <david.monichi@smooth-systems.solutions> on
 * 29.05.18.
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

		List<ProductId> products = initializeProductCacheAndRetrieveList();
		log.info("Read products and initialized cache");

		List<MigrationProductImagesObject> imagesInfo = generateProductImagesObjects(products);
		log.info("Generated information objects to start images upload for main products.");

		uploadProductImages(imagesInfo);
	}

	private void uploadProductImages(List<MigrationProductImagesObject> imagesInfo) {
		MigrationSystemWriter writer = readerWriterFactory.getMigrationWriter();
		for(MigrationProductImagesObject imageObject : imagesInfo) {
			log.info("Upload product images: {}", imageObject);
			for(File imageUrl : imageObject.getImageUrls()) {
				writer.uploadProductImages(imageObject.getDstProductId(), imageUrl);
			}
		}
	}

	private List<ProductId> initializeProductCacheAndRetrieveList() {
		List<ProductId> mainProductIds = new ArrayList<>();
		try {
			for (Long prodId : productIdsSourceSystem.keySet()) {
				Long mainProductId = productIdsSourceSystem.getMappedIdForId(prodId);
				ProductId product = ProductId.builder().productId(mainProductId).langIso(config.getRootCategoryLanguage()).build();
				mainProductIds.add(product);
			}
		} catch(NotFoundException e) {
			throw new IllegalStateException("This exception shouldn't not happen.");
		}
		productsCache = ProductsCache.createProductsCache(readerWriterFactory.getMigrationReader(), mainProductIds);
		return mainProductIds;
	}

	private List<MigrationProductImagesObject> generateProductImagesObjects(List<ProductId> products) {
		File imagesUrl = new File(config.getProductsImagesDirectory());
		List<MigrationProductImagesObject> imagesObjects = new ArrayList<>();
		products.forEach(prod -> {
			try {
				Product product = productsCache.getProductById(prod.getProductId());
				List<String> imageUrls = product.getProductImageUrls();
				List<File> absoluteImagesPaths = imageUrls.stream().map(url -> new File(imagesUrl, url)).collect(Collectors.toList());
				MigrationProductImagesObject imagesObj = new MigrationProductImagesObject(prod.getProductId(), absoluteImagesPaths);

				Long dstProductId = productIdsMigration.getMappedIdForId(prod.getProductId());
				imagesObj.setDstProductId(dstProductId);
				imagesObjects.add(imagesObj);
			} catch (NotFoundException e) {
				log.error("Error while loading product with id {}.", prod.getProductId());
				throw new IllegalStateException("Error while loading products.", e);
			}
		});
		return imagesObjects;
	}

	protected void initialize() {
		super.initialize();
		productIdsMigration.initializeIdMapperFromFile();
	}
}