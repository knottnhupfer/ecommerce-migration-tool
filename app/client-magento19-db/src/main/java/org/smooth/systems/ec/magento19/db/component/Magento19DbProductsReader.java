package org.smooth.systems.ec.magento19.db.component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

import org.smooth.systems.ec.client.util.ObjectIdMapper;
import org.smooth.systems.ec.configuration.MigrationConfiguration;
import org.smooth.systems.ec.magento19.db.model.Magento19Product;
import org.smooth.systems.ec.magento19.db.model.Magento19ProductText;
import org.smooth.systems.ec.magento19.db.model.Magento19ProductTierPrice;
import org.smooth.systems.ec.magento19.db.model.Magento19ProductVarchar;
import org.smooth.systems.ec.magento19.db.repository.ProductRepository;
import org.smooth.systems.ec.magento19.db.repository.ProductTierPriceRepository;
import org.smooth.systems.ec.migration.model.*;
import org.smooth.systems.ec.migration.model.Product.ProductVisibility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import lombok.extern.slf4j.Slf4j;

/**
 * Open topics:
 * <ul>
 * <li>currently only a single category is considered
 * <li>no related products are considered
 * </ul>
 * 
 * Created by David Monichi <david.monichi@smooth-systems.solutions> on
 * 24.02.18.
 */
@Slf4j
@Component
public class Magento19DbProductsReader {

  public static Double DEFAULT_TAX_RATE = new Double("1.22");

  @Autowired
  private ProductRepository productRepo;

  @Autowired
  private ProductTierPriceRepository productTierPriceRepo;

  @Autowired
  private Magento19DbProductFieldsProvider productFieldsProvider;

  @Autowired
  private MigrationConfiguration config;

  // maps a categoryId from source system to a created categoryId at the destination system  
  private ObjectIdMapper categoryIdMapper;

  public void init() {
    if(categoryIdMapper == null) {
      String categoriesMappingFile = config.getGeneratedCreatedCategoriesMappingFile();
      Assert.hasText(categoriesMappingFile, "generated-created-categories-mapping-file is empty");
      categoryIdMapper = new ObjectIdMapper(categoriesMappingFile);
      categoryIdMapper.initializeIdMapperFromFile();
    }
  }

  public Product getMergedProduct(List<Pair<Long, String>> productDefinitions) {
    log.info("getProduct({})", productDefinitions);
    Assert.notEmpty(productDefinitions, "no products to be merged");
    Pair<Long, String> rootProduct = productDefinitions.get(0);
    log.info("Create productId: {} with language: {}", rootProduct.getFirst(), rootProduct.getSecond());
    Product product = getProduct(rootProduct.getFirst(), rootProduct.getSecond());
    
    List<Pair<Long, String>> toBeMergedList = productDefinitions.subList(1, productDefinitions.size());
    toBeMergedList.forEach(productPair -> {
      log.info("Merge productId: {} with language: {}", productPair.getFirst(), productPair.getSecond());
      ProductTranslateableAttributes attributes = getTranslateablAttributesForProduct(productPair.getFirst(), productPair.getSecond());
      product.getAttributes().add(attributes);
    });
    return product;
  }

  public Product getProduct(Long productId, String langCode) {
    Assert.notNull(productId, "productId is null");

    Magento19Product retrievedProduct = productRepo.findById(productId);
    Assert.notNull(retrievedProduct, "no product found for id " + productId);
    LocalDateTime creationDate = LocalDateTime.ofInstant(retrievedProduct.getCreatedAt().toInstant(), ZoneId.systemDefault());
    Product product = new Product(retrievedProduct.getId(), retrievedProduct.getSku(), creationDate);

    updateProductPrice(product);
    updateCategories(product);
    updateProductVisibility(product);
    updateProductManufacturer(product);
    updateDimensionAndShippingForProduct(product);
    updateImagesUrls(product);
    updateProductAttributes(product);

    product.getAttributes().add(getTranslateablAttributesForProduct(productId, langCode));
    return product;
  }

  public ProductPriceStrategies getProductPriceStrategy(Long productId) {
    ProductPriceStrategies strategies = new ProductPriceStrategies();
    List<Magento19ProductTierPrice> tierPrices = productTierPriceRepo.findByProductId(productId);
    strategies.setProductId(productId);
    for (Magento19ProductTierPrice tierPrice : tierPrices) {
      strategies.addTierPriceStrategy(convertMagentoTierPriceStrategy(tierPrice));
    }
    return strategies;
  }

  private void updateProductAttributes(Product product) {
    Assert.notNull(product,"product is null");
    Assert.notNull(product.getId(),"product id is null");
    product.setActivated(productFieldsProvider.getProductAttributeIdActivated(product.getId()));
  }

  private ProductTranslateableAttributes getTranslateablAttributesForProduct(Long productId, String langCode) {
    ProductTranslateableAttributes attributes = new ProductTranslateableAttributes(langCode);
    attributes.setDescription(productFieldsProvider.getTextAttribute(productId, Magento19ProductText.DESCRIPTION_ATTR_ID));
    attributes.setFriendlyUrl(productFieldsProvider.getVarcharAttribute(productId, Magento19ProductVarchar.FRIENDLY_URL_ATTR_ID));
    attributes.setName(productFieldsProvider.getVarcharAttribute(productId, Magento19ProductVarchar.NAME_ATTR_ID));
    attributes.setShortDescription(productFieldsProvider.getTextAttribute(productId, Magento19ProductText.SHORT_DESCRIPTION_ATTR_ID));
    String tagsList = productFieldsProvider.getTextAttribute(productId, Magento19ProductText.META_TAGS_ATTR_ID);
    attributes.setTags(productFieldsProvider.getStringList(tagsList));
    return attributes;
  }

  private void updateProductManufacturer(Product product) {
    product.setBrandId(productFieldsProvider.getBrandIdForProduct(getId(product)));
    logRetrievedValue("brandId", product.getBrandId(), product);
  }

  private void updateDimensionAndShippingForProduct(Product product) {
    Double weight = productFieldsProvider.getProductWeight(getId(product));
    ProductDimensionAndShipping productDetails = new ProductDimensionAndShipping();
    productDetails.setWeight(weight);
    // TODO dimensions of product
    product.setDimension(productDetails);
    logRetrievedValue("DimensionAndShipping", product.getDimension(), product);
  }

  private void updateProductVisibility(Product product) {
    ProductVisibility visibility = productFieldsProvider.getProductVisibility(getId(product));
    product.setVisibility(visibility);
    logRetrievedValue("visibility", product.getVisibility(), product);
  }

  private void updateProductPrice(Product product) {
    Double grossPrice = productFieldsProvider.getProductGrossPrice(getId(product));
    Double netPrice = Math.round(grossPrice / DEFAULT_TAX_RATE * 100.0) / 100.0;
    product.setNetPrice(netPrice);
    logRetrievedValue("sales price", product.getNetPrice(), product);
  }

  private void updateCategories(Product product) {
    List<Long> categories = productFieldsProvider.getCategoryIdForProductId(getId(product));
    product.setCategories(categories);
    logRetrievedValue("categoryId", product.getCategories(), product);
  }

  private void updateImagesUrls(Product product) {
    String mainImageUrl = productFieldsProvider.getMainImageUrlOfProductId(getId(product));
    List<String> imageUrls = productFieldsProvider.getImageUrlsOfProductId(getId(product));
    imageUrls.remove(mainImageUrl);
    imageUrls.add(0, mainImageUrl);
    // TODO as fast bug fix this works fine, should consider to implement a better strategy
    imageUrls = imageUrls.stream().filter(url -> !url.contains("no_selection")).collect(Collectors.toList());
    product.getProductImageUrls().addAll(imageUrls);
    logRetrievedValue("imageUrls", product.getProductImageUrls(), product);
  }

  private Long getId(Product product) {
    Assert.notNull(product.getId(), "productId is null");
    return product.getId();
  }

  private void logRetrievedValue(String valueName, Object value, Product product) {
    log.trace("Retrieved product {}: {} for productId: {}", valueName, value, product.getId());
  }

  private ProductTierPriceStrategy convertMagentoTierPriceStrategy(Magento19ProductTierPrice strategy) {
    ProductTierPriceStrategy productPriceStrategy = new ProductTierPriceStrategy();
    productPriceStrategy.setId(strategy.getId());
    // TODO do not know yet
    productPriceStrategy.setDiscountTaxIncluded(true);
    productPriceStrategy.setDiscountType(ProductTierPriceStrategy.DiscountType.PRICE);
    productPriceStrategy.setValue(strategy.getPrice());
    productPriceStrategy.setMinQuantity(strategy.getQuantity().longValue());
    return productPriceStrategy;
  }
}