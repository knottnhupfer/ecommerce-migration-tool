package org.smooth.systems.ec.utils.db.component;

import lombok.extern.slf4j.Slf4j;
import org.smooth.systems.ec.client.api.CategoryConfig;
import org.smooth.systems.ec.client.util.ObjectIdMapper;
import org.smooth.systems.ec.client.util.ObjectStringMapper;
import org.smooth.systems.ec.configuration.MigrationConfiguration;
import org.smooth.systems.ec.utils.db.api.IActionExecuter;
import org.smooth.systems.ec.utils.db.model.MagentoProduct;
import org.smooth.systems.ec.utils.db.repository.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions> on 03.02.18.
 */
@Slf4j
@Component
public class ProductsMergeMappingListExecutor extends AbstractProductsForCategoryReader implements IActionExecuter {

	@Autowired
	private MigrationConfiguration config;

	@Autowired
	private ProductsRepository productsRepo;

	private Map<String, MagentoProduct> productMap = new HashMap<>();

	private ObjectIdMapper productIdsMapper;

	@Override
	public String getActionName() {
		return "merge-products";
	}

	@Override
	public void execute() {
		log.trace("execute()");
		initializeRootCategories();

		for (CategoryConfig categoryConfig : config.getAdditionalCategories()) {
			log.info("Merge category: {}", categoryConfig);
			mergeProductIdsToRootProducts(categoryConfig);
		}
		productIdsMapper.writeMappingToFile(" mapping alternative language categoryId to rootCategoryId");
	}

	private void initializeRootCategories() {
		List<MagentoProduct> rootProducts = getCategoryProducts(config.getRootCategoryId());
		log.info("Root products: {}", rootProducts);
		checkIfProductsAreValid("RootProducts", rootProducts);
		productIdsMapper = new ObjectIdMapper(config.getGeneratedProductsMergingFile());
		rootProducts.forEach(product -> productMap.put(product.getSku(), product));
	}

	private void mergeProductIdsToRootProducts(CategoryConfig categoryConfig) {
		log.info("mergeProductIdsToRootProducts({})", categoryConfig);
		List<MagentoProduct> products = getCategoryProducts(categoryConfig.getCategoryId());
		checkIfProductsAreValid("Category: " + categoryConfig, products);
		log.info("{} products to be merged", products.size());

		products.forEach(product -> {
			String productSku = beautifyProductSku(product);
			if(productMap.keySet().contains(productSku)) {
				MagentoProduct matchingRootProduct = productMap.get(productSku);
				productIdsMapper.addMapping(product.getId(), matchingRootProduct.getId());
			}
		});
		products.removeIf(product -> productIdsMapper.keySet().contains(product.getId()));
		log.info("{} not merged products", products.size());
		if(!products.isEmpty()) {
			log.error("Not merged products: {}", products);
			throw new RuntimeException(String.format("Unable to merge %s products.", products.size()));
		}
	}

	private String beautifyProductSku(MagentoProduct product) {
		String sku = product.getSku().trim();
		if(sku.startsWith("_")) {
			sku = sku.substring(1);
		}
		return sku;
	}

	private void checkIfProductsAreValid(String info, List<MagentoProduct> products) {
		log.debug("checkIfProductsAreValid({}, {})", info, products.size());
		products.removeIf(product -> config.getProductIdsSkipping().contains(product.getId()));
		List<MagentoProduct> invalidProducts = products.stream().filter(product -> product.getSku() == null || product.getSku().isEmpty()).collect(Collectors.toList());
		if(!invalidProducts.isEmpty()) {
			String msg = String.format("%s contains %s invalid products.", info, products.size());
			log.error(msg);
			log.error("Invalid products: {}", invalidProducts);
			throw new RuntimeException(msg);
		}
	}

	private List<MagentoProduct> getCategoryProducts(Long categoryId) {
		log.info("getCategoryProducts({})", categoryId);
		List<Long> rootProductIds = retrieveProductIdsForCateoryId(categoryId);
		log.info("Retrieved {} product ids for category id {}", rootProductIds.size(), categoryId);
		return productsRepo.getAllProductsForIds(rootProductIds);
	}

	private void printoutProductWithEmptySKU(List<MagentoProduct> products, String prefix) {
		products.forEach(product -> {
			if(product.getSku() == null) {
				log.info("{}: NULL: id: {}, SKU: {}", prefix, product.getId(), product.getSku());
			}
		});
	}
}
