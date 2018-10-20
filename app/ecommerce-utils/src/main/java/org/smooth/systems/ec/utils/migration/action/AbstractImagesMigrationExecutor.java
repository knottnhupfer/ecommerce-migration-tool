package org.smooth.systems.ec.utils.migration.action;

import lombok.extern.slf4j.Slf4j;
import org.smooth.systems.ec.migration.model.IProductMetaData;
import org.smooth.systems.ec.migration.model.Product;
import org.smooth.systems.ec.utils.migration.model.MigrationProductImagesObject;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public abstract class AbstractImagesMigrationExecutor extends AbstractProductsMigrationExecuter {

	private List<Long> productIdsWithImages;

	public AbstractImagesMigrationExecutor(String actionName) {
		super(actionName);
	}

	protected boolean hasProductNoUploadedImages(IProductMetaData iProductMetaData) {
		return hasProductNoUploadedImages(iProductMetaData.getSku());
	}

	protected void uploadImageIfNotAlreadyUploaded(String sku) {
		if(hasProductNoUploadedImages(sku)) {
			IProductMetaData productMetaData = getProductsMetaDataFromDestinationSystemForSku(sku);
			log.info("Upload images for product with sku '{}' to dst product id: '{}'", sku, productMetaData.getProductId());
			uploadProductImagesWithSku(sku, productMetaData.getProductId());
		} else {
			log.debug("Images for product with sku '{}' already uploaded.", sku);
		}
	}

	private void uploadProductImagesWithSku(String sku, Long productIdDestinationSystem) {
		log.debug("uploadProductImagesWithSku({}, {})", sku, productIdDestinationSystem);
		Product productSourceSystem = srcProductsCache.getProductBySku(sku);
		MigrationProductImagesObject productImagesObject = generateProductImagesObject(productIdDestinationSystem, productSourceSystem);
		uploadProductImages(productImagesObject);
	}

	private boolean hasProductNoUploadedImages(String sku) {
		IProductMetaData dstProductMetaData = getProductsMetaDataFromDestinationSystemForSku(sku);
		populateProductIdsListWithImagesIfNotDoneYet();
		return !productIdsWithImages.contains(dstProductMetaData.getProductId());
	}

	private void populateProductIdsListWithImagesIfNotDoneYet() {
		if(productIdsWithImages != null) {
			return;
		}
		log.info("Read products with uploaded images ...");
		productIdsWithImages = getReaderForDestinationSystem().readAllProductsImagesProductIds();
		log.info("Read products with uploaded images ... DONE");
	}

	private MigrationProductImagesObject generateProductImagesObject(Long productIdDestinationSystem, Product product) {
		File imagesUrl = new File(config.getProductsImagesDirectory());
		List<String> imageUrls = product.getProductImageUrls();
		List<File> absoluteImagesPaths = imageUrls.stream().map(url -> new File(imagesUrl, url)).collect(Collectors.toList());
		return new MigrationProductImagesObject(product.getId(), productIdDestinationSystem, absoluteImagesPaths);
	}

	private void uploadProductImages(MigrationProductImagesObject productImagesInfo) {
		log.info("Upload product images: {}", productImagesInfo);
		for (File imageUrl : productImagesInfo.getImageUrls()) {
			writerDstSystem.uploadProductImages(productImagesInfo.getDstProductId(), imageUrl);
		}
		log.info("Uploaded successfully product images from product with id: {} to destination id: {}",
			productImagesInfo.getSrcProductId(), productImagesInfo.getDstProductId());
	}
}
