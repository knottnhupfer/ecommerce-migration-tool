package org.smooth.systems.ec.utils.migration.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.smooth.systems.ec.client.api.MigrationSystemWriter;
import org.smooth.systems.ec.client.api.SimpleProduct;
import org.smooth.systems.ec.client.util.ObjectIdMapper;
import org.smooth.systems.ec.component.MigrationSystemReaderAndWriterFactory;
import org.smooth.systems.ec.configuration.MigrationConfiguration;
import org.smooth.systems.ec.exceptions.NotFoundException;
import org.smooth.systems.ec.migration.model.Product;
import org.smooth.systems.ec.migration.model.ProductTranslateableAttributes;
import org.smooth.systems.ec.utils.db.api.IActionExecuter;
import org.smooth.systems.ec.utils.db.component.AbstractProductsForCategoryReader;
import org.smooth.systems.ec.utils.migration.component.ProductsCache;
import org.smooth.systems.ec.utils.migration.model.MigrationProductData;
import org.smooth.systems.ec.utils.migration.util.ProductMigrationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions> on
 * 29.05.18.
 */
@Slf4j
@Component
public class MigrateProductsExecutor extends AbstractProductsForCategoryReader implements IActionExecuter {

	@Autowired
	private MigrationConfiguration config;

	@Autowired
	private MigrationSystemReaderAndWriterFactory readerWriterFactory;

	private ProductsCache productsCache;

	/**
	 * Maps the product id from the alternative language to the product id of the
	 * main language id (only source system)
	 *
	 * <ul>
	 * <li>key ... product id german language source system</li>
	 * <li>value ... main product id source system (italian language)</li>
	 * </ul>
	 */
	private ObjectIdMapper productIdsSourceSystem;

	/**
	 * Maps the product id from the source system to the product id of the
	 * uploaded product
	 *
	 * <ul>
	 * <li>key ... product id source system</li>
	 * <li>value ... product id destination system</li>
	 * </ul>
	 */
	private ObjectIdMapper productIdsMigration;

	@Override
	public String getActionName() {
		return "products-migrate";
	}

	@Override
	public void execute() {
		initialize();
		log.trace("execute()");

		List<MigrationProductData> productList = generateProductsList("it", "de");
		log.info("Generated basic product data ({}) for migration successfully", productList.size());

		initializeProductsCache(productList);
		log.info("Products cache initialized successfully");

		List<Product> mergedProducts = mergeProductsLanguageAttributes(productList);
		log.info("Successfully merged {} products", mergedProducts.size());
		log.trace("Merged Products:");
		for (Product prod : mergedProducts) {
			log.trace("   " + prod);
		}

		ProductMigrationUtils.updateCategoryIdWithDestinationSystemCategoryId(config, mergedProducts);
		log.info("Products category updated");

		List<Product> filledUpProducts = fillUpProductsWithMissingLanguage(mergedProducts);
		log.info("Successfully filled up {} products", filledUpProducts.size());

		uploadProductsAndWriteMapping(filledUpProducts);
		log.info("Successfully migrated all products");
	}

	private void initializeProductsCache(List<MigrationProductData> productList) {
		List<SimpleProduct> collect = new ArrayList<>();
		productList.stream().map(data -> data.getAsFullList()).collect(Collectors.toList()).stream().forEach(x -> collect.addAll(x));
		productsCache = ProductsCache.createProductsCache(readerWriterFactory.getMigrationReader(), collect);
	}

	private List<MigrationProductData> generateProductsList(String mainLangCode, String alternativeLangCode) {
		List<MigrationProductData> products = new ArrayList<>();

		Set<Long> alternativeProductIds = productIdsSourceSystem.keySet();
		try {
			for (Long prodId : alternativeProductIds) {
				SimpleProduct mainProduct = SimpleProduct.builder().productId(productIdsSourceSystem.getMappedIdForId(prodId)).langIso(mainLangCode)
					.build();
				SimpleProduct altProduct = SimpleProduct.builder().productId(prodId).langIso(alternativeLangCode).build();
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
		SimpleProduct mainProdInfo = productData.getMainProduct();
		Product product = productsCache.getProductById(mainProdInfo.getProductId());
		Assert.isTrue(product.getAttributes().size() == 1 && mainProdInfo.getLangIso().equals(product.getAttributes().get(0).getLangCode()),
			"invalid attributes configuration");

		for (SimpleProduct altProductInfo : productData.getAlternativeProducts()) {
			Assert.isTrue(!product.getAttributes().stream().filter(attr -> attr.getLangCode().equals(altProductInfo.getLangIso())).findAny().isPresent(),
				String.format("Duplicated attribute language for language: %s", altProductInfo.getLangIso()));
			Product altProduct = productsCache.getProductById(altProductInfo.getProductId());
			Assert.isTrue(altProduct.getAttributes().size() == 1, "More then a single language in alternative product.");
			ProductTranslateableAttributes attr = altProduct.getAttributes().get(0);
			Assert.isTrue(altProductInfo.getLangIso().equals(attr.getLangCode()), "Languages do not match with each other.");
			product.getAttributes().add(attr);
		}
		return product;
	}

	private List<Product> fillUpProductsWithMissingLanguage(List<Product> productList) {
		// TODO: check if all products are filled with 2 languages
		// TODO: what to do with only Italian products, not in list
		// TODO: what to do with only German products, not in list and filtered earlier
		// throw new NotImplementedException();
		log.warn("Not mapped italian and german products are currently skipped.");
		return productList;
	}

	private void uploadProductsAndWriteMapping(List<Product> productsToBeWrittern) {
		MigrationSystemWriter writer = readerWriterFactory.getMigrationWriter();
		for (Product product : productsToBeWrittern) {
			Long srcProdId = product.getId();
			Product writtenProduct = writer.writeProduct(product);
			productIdsMigration.addMapping(srcProdId, writtenProduct.getId());
		}
		productIdsMigration.writeMappingToFile("Mapping file which maps product ids from source system to product ids to destination system");
	}

	private void initialize() {
		log.info("Read product merging mapping from: {}", config.getGeneratedProductsMergingFile());
		productIdsSourceSystem = new ObjectIdMapper(config.getGeneratedProductsMergingFile());
		productIdsSourceSystem.initializeIdMapperFromFile();
		productIdsMigration = new ObjectIdMapper(config.getGeneratedProductsMigrationFile());
	}
}