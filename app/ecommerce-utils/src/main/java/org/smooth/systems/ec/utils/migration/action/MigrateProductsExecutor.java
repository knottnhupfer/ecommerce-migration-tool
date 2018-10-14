package org.smooth.systems.ec.utils.migration.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.smooth.systems.ec.client.api.MigrationSystemWriter;
import org.smooth.systems.ec.client.api.ProductId;
import org.smooth.systems.ec.exceptions.NotFoundException;
import org.smooth.systems.ec.migration.model.Product;
import org.smooth.systems.ec.migration.model.ProductTranslateableAttributes;
import org.smooth.systems.ec.utils.EcommerceUtilsActions;
import org.smooth.systems.ec.utils.migration.component.ProductsCache;
import org.smooth.systems.ec.utils.migration.model.MigrationProductData;
import org.smooth.systems.ec.utils.migration.util.ProductMigrationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions>
 */
@Slf4j
@Component
public class MigrateProductsExecutor extends AbstractProductsMigrationExecuter {

	public MigrateProductsExecutor() {
		super(EcommerceUtilsActions.PRODUCTS_MIGRATION);
	}

	@Override
	public void execute() {
		initialize();
		log.trace("execute()");

		List<MigrationProductData> productList = generateProductsList();
		logExecutionStep(log, "Generated basic product data ({}) for migration successfully", productList.size());

		initializeProductsCache(productList);
		logExecutionStep(log, "Read products and initialized cache");

		List<Product> mergedProducts = mergeProductsLanguageAttributes(productList);
		logExecutionStep(log, "Successfully merged {} products", mergedProducts.size());
		log.trace("Merged Products:");
		for (Product prod : mergedProducts) {
			log.trace("   " + prod);
		}

		ProductMigrationUtils.updateCategoryIdWithDestinationSystemCategoryId(config, mergedProducts);
		logExecutionStep(log, "Products category updated");

		List<Product> updatedProducts = processNotMergedProducts(mergedProducts);

		updatedProducts = fillUpProductsWithMissingLanguage(updatedProducts);
		logExecutionStep(log, "Successfully filled up {} products", updatedProducts.size());

		uploadProductsAndWriteMapping(updatedProducts);
		logExecutionStep(log, "Successfully migrated all products");
	}

	protected List<MigrationProductData> generateProductsList() {
		return generateProductsList("it", "de");
	}

	private void initializeProductsCache(List<MigrationProductData> productList) {
		List<ProductId> collect = new ArrayList<>();
		productList.stream().map(data -> data.getAsFullList()).collect(Collectors.toList()).stream().forEach(x -> collect.addAll(x));
		srcProductsCache = ProductsCache.createProductsCache(readerWriterFactory.getMigrationReader(), collect);
	}

	private List<MigrationProductData> generateProductsList(String mainLangCode, String alternativeLangCode) {
		List<MigrationProductData> products = new ArrayList<>();

		Set<Long> alternativeProductIds = productIdsSourceSystem.keySet();
		try {
			for (Long prodId : alternativeProductIds) {
				ProductId mainProduct = ProductId.builder().productId(productIdsSourceSystem.getMappedIdForId(prodId)).langIso(mainLangCode)
					.build();
				ProductId altProduct = ProductId.builder().productId(prodId).langIso(alternativeLangCode).build();
				products.add(new MigrationProductData(mainProduct, altProduct));
			}
		} catch (NotFoundException e) {
			log.error("Error while preparing data, actually can't happen!");
			throw new IllegalStateException("Error while preparing data, actually can't happen!");
		}
		return products;
	}

	private List<Product> mergeProductsLanguageAttributes(List<MigrationProductData> productList) {
		List<Product> products = new ArrayList<>();
		for (MigrationProductData productData : productList) {
			Product product = populateProduct(productData);
			products.add(product);
		}
		return products;
	}

	private Product populateProduct(MigrationProductData productData) {
		ProductId mainProdInfo = productData.getMainProduct();
		Product product = srcProductsCache.getProductById(mainProdInfo.getProductId());
		Assert.isTrue(product.getAttributes().size() == 1 && mainProdInfo.getLangIso().equals(product.getAttributes().get(0).getLangCode()),
			"invalid attributes configuration");

		for (ProductId altProductInfo : productData.getAlternativeProducts()) {
			Assert.isTrue(!product.getAttributes().stream().filter(attr -> attr.getLangCode().equals(altProductInfo.getLangIso())).findAny().isPresent(),
				String.format("Duplicated attribute language for language: %s", altProductInfo.getLangIso()));
			Product altProduct = srcProductsCache.getProductById(altProductInfo.getProductId());
			Assert.isTrue(altProduct.getAttributes().size() == 1, "More then a single language in alternative product.");
			ProductTranslateableAttributes attr = altProduct.getAttributes().get(0);
			Assert.isTrue(altProductInfo.getLangIso().equals(attr.getLangCode()), String.format("Languages do not match with each other. Product data is: %s", productData));
			product.getAttributes().add(attr);
		}
		product.getAttributes().forEach(this::replaceNewlinesAttributesValues);
		return product;
	}

	private List<Product> fillUpProductsWithMissingLanguage(List<Product> productList) {
		// TODO: check if all products are filled with 2 languages
		log.info("Successfully filled up {} products", productList.size());
		return productList;
	}

	private List<Product> processNotMergedProducts(List<Product> productList) {
		// TODO: what to do with only Italian products, not in list
		// TODO: what to do with only German products, not in list and filtered earlier
		log.warn("Not mapped italian and german products are currently skipped.");
//		log.info("Successfully processed up {} products", productList.size());
		return productList;
	}

	private void uploadProductsAndWriteMapping(List<Product> productsToBeWrittern) {
		long skippedProducts = 0;
		MigrationSystemWriter writer = readerWriterFactory.getMigrationWriter();
		for (Product product : productsToBeWrittern) {
			if(!doesProductWithSkuExists(product.getSku())) {
				writer.writeProduct(product);
			} else {
				log.debug("Product with sku {} already exists on destination system.", product.getSku());
				skippedProducts++;
			}
		}
		log.info("Migrate {} products: {} products created and {} products skipped.", productsToBeWrittern.size(),
						productsToBeWrittern.size() - skippedProducts, skippedProducts);
	}

	private void replaceNewlinesAttributesValues(ProductTranslateableAttributes attr) {
		attr.setDescription(replaceNewlines(attr.getDescription()));
		attr.setShortDescription(replaceNewlines(attr.getShortDescription()));
	}

	private String replaceNewlines(String value) {
		String replacedValue = value.replaceAll("\r\n", "<br>");
		return replacedValue.replaceAll("\n", "<br>");
	}
}
