package org.smooth.systems.ec.magento19.db.component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.smooth.systems.ec.client.util.ObjectIdMapper;
import org.smooth.systems.ec.configuration.MigrationConfiguration;
import org.smooth.systems.ec.magento19.db.model.Magento19Product;
import org.smooth.systems.ec.magento19.db.model.Magento19ProductText;
import org.smooth.systems.ec.magento19.db.model.Magento19ProductVarchar;
import org.smooth.systems.ec.magento19.db.repository.ProductRepository;
import org.smooth.systems.ec.migration.model.Product;
import org.smooth.systems.ec.migration.model.Product.ProductVisibility;
import org.smooth.systems.ec.migration.model.ProductDimensionAndShipping;
import org.smooth.systems.ec.migration.model.ProductTranslateableAttributes;
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

  public static Double DEFAULT_TAX_RATE = new Double("1.21");

  @Autowired
  private ProductRepository productRepo;

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
    updateMainCategoryId(product);
    updateProductVisibility(product);
    updateProductManufacturer(product);
    updateDimensionAndShippingForProduct(product);
    updateImagesUrls(product);

    product.getAttributes().add(getTranslateablAttributesForProduct(productId, langCode));
    return product;
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
    Double salesPrice = productFieldsProvider.getProductSalesPrice(getId(product));
    product.setSalesPrice(salesPrice);
    Double costPrice = Math.round(salesPrice / DEFAULT_TAX_RATE * 100.0) / 100.0;
    product.setCostPrice(costPrice);
    logRetrievedValue("sales price", product.getSalesPrice(), product);
    logRetrievedValue("cost price", product.getCostPrice(), product);
  }

  private void updateMainCategoryId(Product product) {
    Long categoryId = productFieldsProvider.getCategoryIdForProductId(getId(product));
    product.getCategories().add(categoryId);
    logRetrievedValue("categoryId", product.getCategories(), product);
  }

  private void updateImagesUrls(Product product) {
    String mainImageUrl = productFieldsProvider.getMainImageUrlOfProductId(getId(product));
    List<String> imageUrls = productFieldsProvider.getImageUrlsOfProductId(getId(product));
    imageUrls.remove(mainImageUrl);
    imageUrls.add(0, mainImageUrl);
    product.getProductImageUrls().addAll(imageUrls);
    logRetrievedValue("imageUrls", product.getProductImageUrls(), product);
  }

//  private void updateCategoryIdWithDestinationCategoryId(Product product) {
//    init();
//    Assert.notEmpty(product.getCategories(), "product categories are empty");
//    try {
//      Long origCategoryId = product.getCategories().get(0);
//      Long createdCategoryId = categoryIdMapper.getMappedIdForId(origCategoryId);
//      product.setCategories(Collections.singletonList(createdCategoryId));
//      log.trace("Updated category id {} to created category id {}", origCategoryId, createdCategoryId);
//    } catch (NotFoundException e) {
//      String msg = String.format("Unable to find created category for source ids %s", product.getCategories());
//      log.error(msg);
//      throw new IllegalStateException(msg);
//    }
//  }

  private Long getId(Product product) {
    Assert.notNull(product.getId(), "productId is null");
    return product.getId();
  }

  private void logRetrievedValue(String valueName, Object value, Product product) {
    log.trace("Retrieved product {}: {} for productId: {}", valueName, value, product.getId());
  }
}