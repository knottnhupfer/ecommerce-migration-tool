package org.smooth.systems.ec.utils.migration.action;

import lombok.extern.slf4j.Slf4j;
import org.smooth.systems.ec.migration.model.IProductMetaData;
import org.smooth.systems.ec.migration.model.Product;
import org.smooth.systems.ec.migration.model.ProductMapping;
import org.smooth.systems.ec.migration.model.RelatedProducts;
import org.smooth.systems.ec.utils.EcommerceUtilsActions;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions>
 */
@Slf4j
@Component
public class MigrateMissingRelatedProductsExecutor extends AbstractProductsMigrationExecuter {

	private List<Pair<ProductMapping,ProductMapping>> migrationMissingProducts = new ArrayList<>();

	public MigrateMissingRelatedProductsExecutor() {
		super(EcommerceUtilsActions.PRODUCTS_RELATED_PRODUCTS_MISSING);
	}

	@Override
	public void execute() {
		initialize();
		initializeEmptySourceSystemProductCache();
		log.info("Initialized readers and writer ...");

		List<IProductMetaData> productsWithoutRelatedProducts = filterDestinationProductsWithNoRelatedProducts();
		log.info("Uploading {} related products.", productsWithoutRelatedProducts.size());

		long index = 0L;
		for(IProductMetaData dstProductMetaData : productsWithoutRelatedProducts) {
			if(srcProductsCache.existsProductWithSku(dstProductMetaData.getSku())) {
				fetchAndUploadRelatedProducts(dstProductMetaData);
				index++;
			}
		}
		log.info("Uploaded {} related products.", index);
		log.info("Skipped products:");
		migrationMissingProducts.forEach(product-> {
			log.info("  - Skipped related product: " + product);
		});
	}

	private void fetchAndUploadRelatedProducts(IProductMetaData prodMetaData) {
		log.debug("fetchAndUploadRelatedProducts({})", prodMetaData);
		Product product = srcProductsCache.getProductBySku(prodMetaData.getSku());
		RelatedProducts relatedProducts = reader.readRelatedProduct(srcProductsCache, product);
		if(!relatedProducts.getRelatedProducts().isEmpty()) {
			log.info("uploadRelatedProductsIdNotExists({})", relatedProducts);
			populateDestinationRelatedProductIds(relatedProducts);
			writer.writeRelatedProducts(relatedProducts);
			log.info("Upload related products for product ... DONE");
		}
	}

	private void populateDestinationRelatedProductIds(RelatedProducts relatedProducts) {
		populateProductMapping(relatedProducts.getProduct());
		List<ProductMapping> missingProducts = new ArrayList<>();
		relatedProducts.getRelatedProducts().forEach( relProduct -> {
			IProductMetaData productMetaData = existingProductsDestinationSystem.get(relProduct.getSku());
			if(productMetaData != null) {
				populateProductMapping(relProduct);
			} else {
				missingProducts.add(relProduct);
				log.warn("Related product {} does not exist.", relProduct);
			}
		});
		relatedProducts.getRelatedProducts().removeAll(missingProducts);
		missingProducts.forEach(product -> {
			migrationMissingProducts.add(Pair.of(relatedProducts.getProduct(), product));
		});
	}

	private void populateProductMapping(ProductMapping productMapping) {
		productMapping.setDstProductId(existingProductsDestinationSystem.get(productMapping.getSku()).getProductId());
	}

	private List<IProductMetaData> filterDestinationProductsWithNoRelatedProducts() {
		List<IProductMetaData> products = getProductsMetaDataFromDestinationSystem();
		log.info("Retrieved {} products from destination system.", products.size());
		return products.stream().filter(prod -> !prod.hasRelatedProducts()).collect(Collectors.toList());
	}
}
