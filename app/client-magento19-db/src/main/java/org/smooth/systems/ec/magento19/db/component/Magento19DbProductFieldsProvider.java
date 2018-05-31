package org.smooth.systems.ec.magento19.db.component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.smooth.systems.ec.client.util.ObjectStringToIdMapper;
import org.smooth.systems.ec.configuration.MigrationConfiguration;
import org.smooth.systems.ec.magento19.db.model.Magento19Category;
import org.smooth.systems.ec.magento19.db.model.Magento19EavAttributeOption;
import org.smooth.systems.ec.magento19.db.model.Magento19ProductDecimal;
import org.smooth.systems.ec.magento19.db.model.Magento19ProductIndexEav;
import org.smooth.systems.ec.magento19.db.model.Magento19ProductMediaEntry;
import org.smooth.systems.ec.magento19.db.model.Magento19ProductText;
import org.smooth.systems.ec.magento19.db.model.Magento19ProductVarchar;
import org.smooth.systems.ec.magento19.db.model.Magento19ProductVisibility;
import org.smooth.systems.ec.magento19.db.repository.CategoryRepository;
import org.smooth.systems.ec.magento19.db.repository.EavAttributeOptionsRepository;
import org.smooth.systems.ec.magento19.db.repository.ProductCategoryMappingRepository;
import org.smooth.systems.ec.magento19.db.repository.ProductDecimalRepository;
import org.smooth.systems.ec.magento19.db.repository.ProductEavIndexRepository;
import org.smooth.systems.ec.magento19.db.repository.ProductMediaEntryRepository;
import org.smooth.systems.ec.magento19.db.repository.ProductTextRepository;
import org.smooth.systems.ec.magento19.db.repository.ProductVarcharRepository;
import org.smooth.systems.ec.magento19.db.repository.ProductVisibilityRepository;
import org.smooth.systems.ec.migration.model.Product.ProductVisibility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions> on
 * 24.02.18.
 */
@Slf4j
@Component
public class Magento19DbProductFieldsProvider {

  // table catalog_product_entity_decimal
  public static Long PRODUCT_ATTRIBUTE_ID_PRODUCT_PRICE = 60L;
  public static Long PRODUCT_ATTRIBUTE_ID_PRODUCT_WEIGHT = 65L;

  public static Long DEFAULT_MANUFACTURER_ID = 2L;

  public static ProductVisibility DEFAULT_PRODUCT_VISIBILITY = ProductVisibility.Everywhere;

  @Autowired
  private MigrationConfiguration config;

  @Autowired
  private CategoryRepository categoryRepo;

  @Autowired
  private ProductMediaEntryRepository mediaEntryRepo;

  @Autowired
  private ProductDecimalRepository productDecimalRepo;

  @Autowired
  private ProductTextRepository productTextRepo;

  @Autowired
  private ProductVarcharRepository productVarcharRepo;

  @Autowired
  private ProductEavIndexRepository productEavIndexRepo;

  @Autowired
  private EavAttributeOptionsRepository eavAttributeRepo;

  @Autowired
  private ProductVisibilityRepository productVisibilityRepo;

  @Autowired
  private ProductCategoryMappingRepository productCategoryRepo;

  private ObjectStringToIdMapper brandMapper;

  public void initialize() {
    if(brandMapper == null) {
      log.info("Initialize Magento19DbProductFieldsProvider with brands mapping file: {}", config.getProductsBrandMappingFile());
      brandMapper = new ObjectStringToIdMapper(config.getProductsBrandMappingFile(), DEFAULT_MANUFACTURER_ID);
    }
  }

  public Long getBrandIdForProduct(Long productId) {
    initialize();
    Assert.notNull(productId, "productId is null");
    Magento19ProductIndexEav indexEav = productEavIndexRepo.findManufacturerEntryForProductId(productId);
    if (indexEav == null) {
      log.debug("Unable to find product eav for productId: {}", productId);
      return brandMapper.getDefaultValue();
    }

    Magento19EavAttributeOption productAttribute = eavAttributeRepo.findByOptionId(Long.parseLong(indexEav.getValue()));
    if (productAttribute == null) {
      log.debug("Unable to find product index eav for productId: {}", productId);
      return brandMapper.getDefaultValue();
    }
    return brandMapper.getMappedIdForIdOrDefault(productAttribute.getValue());
  }

  public ProductVisibility getProductVisibility(Long productId) {
    Assert.notNull(productId, "productId is null");
    Magento19ProductVisibility visibility = productVisibilityRepo.findById(productId);
    if (visibility == null) {
      log.trace("Visibility not found for productId: {}, use default product visibility: {}", productId, DEFAULT_PRODUCT_VISIBILITY);
      return DEFAULT_PRODUCT_VISIBILITY;
    }
    return visibility.getVisibility();
  }

  public Double getProductSalesPrice(Long productId) {
    Assert.notNull(productId, "productId is null");
    Magento19ProductDecimal decimalAttribute = productDecimalRepo.findByProductIdAndAttributeId(productId,
        PRODUCT_ATTRIBUTE_ID_PRODUCT_PRICE);
    if (decimalAttribute == null) {
      String msg = String.format("Unable to retrieve gross price for productId: {}", productId);
      log.error(msg);
      throw new IllegalStateException(msg);
    }
    Assert.notNull(decimalAttribute.getValue(), "price is null");
    return decimalAttribute.getValue();
  }

  public Double getProductWeight(Long productId) {
    Assert.notNull(productId, "productId is null");
    Magento19ProductDecimal decimalAttribute = productDecimalRepo.findByProductIdAndAttributeId(productId,
        PRODUCT_ATTRIBUTE_ID_PRODUCT_WEIGHT);
    if (decimalAttribute == null) {
      String msg = String.format("Unable to retrieve weight for productId: {}", productId);
      log.error(msg);
      throw new IllegalStateException(msg);
    }
    Assert.notNull(decimalAttribute.getValue(), "weight is null");
    return decimalAttribute.getValue();
  }

  public Long getCategoryIdForProductId(Long productId) {
    log.debug("getCategoryIdForProductId({})", productId);
    List<Long> categoryIds = productCategoryRepo.getCategoryIdsforProductId(productId);
    if (categoryIds.isEmpty()) {
      String msg = String.format("No categoryIds found for productId:%s", productId);
      log.error(msg);
      throw new RuntimeException(msg);
    }
    log.trace("Found {} categories for productId {}", categoryIds.size(), productId);

    List<Magento19Category> categories = categoryRepo.getByCategoryIdsOrderedByLevel(categoryIds);
    if (categories.isEmpty()) {
      String msg = String.format("No categories found for productId:%s", productId);
      log.error(msg);
      throw new RuntimeException(msg);
    }
    if (categoryIds.size() != categories.size()) {
      log.warn("Unable to fetch proper category size({}), found categories: {}", categoryIds.size(), categories);
    }
    log.trace("Found {} categories for productId {}", categoryIds.size(), productId);

    return categories.get(0).getId();
  }

  public List<String> getImageUrlsOfProductId(Long productId) {
    log.debug("getImageUrlsOfProductId({})", productId);
    List<Magento19ProductMediaEntry> entries = mediaEntryRepo.findByEntityId(productId);
    return entries.stream().map(entry -> entry.getValue()).collect(Collectors.toList());
  }

  public String getMainImageUrlOfProductId(Long productId) {
    log.debug("getMainImageUrlOfProductId({})", productId);
    List<Magento19ProductVarchar> entry = productVarcharRepo.findByEntityIdAndAttributeId(productId,
        Magento19ProductVarchar.MAIN_IMAGE_URL);
    if (entry.isEmpty()) {
      List<String> imageUrls = getImageUrlsOfProductId(productId);
      return imageUrls.get(0);
    }
    return entry.get(0).getValue();
  }

  public String getTextAttribute(Long productId, Long attributeId) {
    log.debug("getTextAttribute({}, {})", productId, attributeId);
    Magento19ProductText entry = productTextRepo.findByEntityIdAndAttributeId(productId, attributeId);
    Assert.notNull(entry, String.format("text attribute with id '%s' is null for product id '%s'", attributeId, productId));
    return entry.getValue();
  }

  public String getVarcharAttribute(Long productId, Long attributeId) {
    log.debug("getVarcharAttribute({}, {})", productId, attributeId);
    List<Magento19ProductVarchar> entries = productVarcharRepo.findByEntityIdAndAttributeId(productId, attributeId);
    Assert.notEmpty(entries, String.format("varchar attribute with id '%s' is null for product id '%s'", attributeId, productId));
    return entries.get(0).getValue();
  }

  public List<String> getStringList(String commaSeparatedStringList) {
    String[] tokens = commaSeparatedStringList.split(",");
    return Arrays.stream(tokens).map(token -> token.trim()).collect(Collectors.toList());
  }
}