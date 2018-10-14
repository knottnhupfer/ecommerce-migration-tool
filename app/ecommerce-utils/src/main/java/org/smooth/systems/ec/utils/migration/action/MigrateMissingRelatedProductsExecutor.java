package org.smooth.systems.ec.utils.migration.action;

import lombok.extern.slf4j.Slf4j;
import org.smooth.systems.ec.migration.model.IProductMetaData;
import org.smooth.systems.ec.migration.model.Product;
import org.smooth.systems.ec.migration.model.RelatedProducts;
import org.smooth.systems.ec.utils.EcommerceUtilsActions;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions>
 */
@Slf4j
@Component
public class MigrateMissingRelatedProductsExecutor extends AbstractProductsMigrationExecuter {

	public MigrateMissingRelatedProductsExecutor() {
		super(EcommerceUtilsActions.PRODUCTS_RELATED_PRODUCTS_MISSING);
	}

	@Override
	public void execute() {
		initialize();
		initializeEmptyProductCache();
		log.info("Initialized readers and writer ...");

		List<IProductMetaData> productsWithoutRelatedProducts = filterDestinationProductsWithNoImages();
		log.info("Uploading {} related products.", productsWithoutRelatedProducts.size());
		productsWithoutRelatedProducts.forEach(prod -> {
			fetchAndUploadRelatedProducts(prod);
		});
	}

	private void fetchAndUploadRelatedProducts(IProductMetaData prodMetaData) {
		log.info("fetchAndUploadRelatedProducts({})", prodMetaData);
		Product product = srcProductsCache.getProductBySku(prodMetaData.getSku());
		RelatedProducts relatedProducts = reader.readRelatedProduct(srcProductsCache, product);
		if(!relatedProducts.getRelatedProducts().isEmpty()) {
			// TODO uncomment upload
//			writer.writeRelatedProducts(relatedProducts);
			log.info("Uploaded related products for product {}", prodMetaData);
		}
	}

	private List<IProductMetaData> filterDestinationProductsWithNoImages() {
		List<IProductMetaData> products = getProductsMetaDataFromDestinationSystem();
		log.info("Retrieved {} products from destination system.", products.size());
		return products.stream().filter(prod -> !prod.hasRelatedProducts()).collect(Collectors.toList());
	}
}
