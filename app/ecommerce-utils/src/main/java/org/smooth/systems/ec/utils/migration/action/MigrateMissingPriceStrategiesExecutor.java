package org.smooth.systems.ec.utils.migration.action;

import lombok.extern.slf4j.Slf4j;
import org.smooth.systems.ec.migration.model.IProductMetaData;
import org.smooth.systems.ec.migration.model.Product;
import org.smooth.systems.ec.migration.model.ProductPriceStrategies;
import org.smooth.systems.ec.utils.EcommerceUtilsActions;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions>
 */
@Slf4j
@Component
public class MigrateMissingPriceStrategiesExecutor extends AbstractProductsMigrationExecuter {

	public MigrateMissingPriceStrategiesExecutor() {
		super(EcommerceUtilsActions.PRODUCTS_PRICE_STRATEGIES_MISSING);
	}

	@Override
	public void execute() {
		initialize();
		initializeEmptySourceSystemProductCache();
		log.info("Initialized readers and writer ...");

		List<IProductMetaData> productsWithMissingPriceStrategies = filterDestinationProductsWithNoTierPrices();
		log.info("Migrate price strategies for {} products", productsWithMissingPriceStrategies.size());

		productsWithMissingPriceStrategies.forEach(product -> {
			uploadProductPriceStrategy(product.getSku());
		});
		log.info("Upload price strategies for products with missing price strategies ... DONE");
	}

	private List<IProductMetaData> filterDestinationProductsWithNoTierPrices() {
		List<ProductPriceStrategies> productPriceStrategiesDstSystem = getReaderForDestinationSystem().readAllProductPriceStrategies();
		List<Long> productIdsWithPriceStrategies = productPriceStrategiesDstSystem.stream().map(ProductPriceStrategies::getProductId).collect(Collectors.toList());

		List<IProductMetaData> products = getProductsMetaDataFromDestinationSystem();
		return products.stream().filter(metaData -> !productIdsWithPriceStrategies.contains(metaData.getProductId())).collect(Collectors.toList());
	}

	private void uploadProductPriceStrategy(String sku) {
		log.debug("uploadProductPriceStrategy()", sku);
		if(!srcProductsCache.existsProductWithSku(sku)) {
			return;
		}

		Product srcSystemProduct = srcProductsCache.getProductBySku(sku);
		IProductMetaData productMetaData = existingProductsDestinationSystem.get(sku);

		ProductPriceStrategies productPriceStrategies = readerSrcSystem.readProductPriceStrategies(srcSystemProduct.getId());
		Assert.notNull(productPriceStrategies.getPriceStrategies(),"price strategies not set");
		if(productPriceStrategies.getPriceStrategies().isEmpty()) {
			return;
		}
		productPriceStrategies.setNetPrice(srcSystemProduct.getNetPrice());
		productPriceStrategies.setProductId(productMetaData.getProductId());
		writerDstSystem.writeProductPriceTier(productPriceStrategies);
		log.info("Written price strategy [sku:{}]: {}", sku, productPriceStrategies);
	}
}