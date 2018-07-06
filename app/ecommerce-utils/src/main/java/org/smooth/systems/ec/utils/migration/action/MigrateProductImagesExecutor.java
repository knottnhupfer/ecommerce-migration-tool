package org.smooth.systems.ec.utils.migration.action;

import lombok.extern.slf4j.Slf4j;
import org.smooth.systems.ec.client.api.MigrationSystemWriter;
import org.smooth.systems.ec.client.api.SimpleProduct;
import org.smooth.systems.ec.component.MigrationSystemReaderAndWriterFactory;
import org.smooth.systems.ec.exceptions.NotFoundException;
import org.smooth.systems.ec.migration.model.Product;
import org.smooth.systems.ec.utils.db.api.IActionExecuter;
import org.smooth.systems.ec.utils.migration.component.ProductsCache;
import org.smooth.systems.ec.utils.migration.model.MigrationProductImagesObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions> on
 * 29.05.18.
 */
@Slf4j
@Component
public class MigrateProductImagesExecutor extends AbstractProductsMigrationExecuter implements IActionExecuter {

	@Autowired
	private MigrationSystemReaderAndWriterFactory readerWriterFactory;

	@Override
	public String getActionName() {
		return "products-image-migrate";
	}

	@Override
	public void execute() {
		initialize();
		log.trace("execute()");

		List<SimpleProduct> products = initializeProductCacheAndRetrieveList();
		log.info("Read products and initialized cache");

		List<MigrationProductImagesObject> imagesInfo = generateProductImagesObjects(products);
		log.info("Generated information objects to start images upload for main products.");

		uploadProductImages(imagesInfo);
	}

	private void uploadProductImages(List<MigrationProductImagesObject> imagesInfo) {
		MigrationSystemWriter writer = readerWriterFactory.getMigrationWriter();
		for (MigrationProductImagesObject imageObject : imagesInfo) {
			for (File imageUrl : imageObject.getImageUrls()) {
				writer.uploadProductImages(imageObject.getDstProductId(), imageUrl);
			}
		}
	}

	private List<SimpleProduct> initializeProductCacheAndRetrieveList() {
		List<SimpleProduct> mainProductIds = new ArrayList<>();
		try {
			for (Long prodId : productIdsSourceSystem.keySet()) {
				Long mainProductId = productIdsSourceSystem.getMappedIdForId(prodId);
				SimpleProduct product = SimpleProduct.builder().productId(mainProductId).langIso(config.getRootCategoryLanguage()).build();
				mainProductIds.add(product);
			}
		} catch (NotFoundException e) {
			throw new IllegalStateException("This exception shouldn't not happen.");
		}
		productsCache = ProductsCache.createProductsCache(readerWriterFactory.getMigrationReader(), mainProductIds);
		return mainProductIds;
	}

	private List<MigrationProductImagesObject> generateProductImagesObjects(List<SimpleProduct> products) {
		File imagesUrl = new File(config.getProductsImagesDirectory());
		List<MigrationProductImagesObject> imagesObjects = new ArrayList<>();
		products.forEach(prod -> {
			Product product = productsCache.getProductById(prod.getProductId());
			List<String> imageUrls = product.getProductImageUrls();
			List<File> absoluteImagesPaths = imageUrls.stream().map(url -> new File(imagesUrl, url)).collect(Collectors.toList());
			MigrationProductImagesObject imagesObj = new MigrationProductImagesObject(prod.getProductId(), absoluteImagesPaths);

			Long dstProductId = getProductIdDestinationSystemForProductSku(product.getSku());
			imagesObj.setDstProductId(dstProductId);
			imagesObjects.add(imagesObj);
		});
		return imagesObjects;
	}
}