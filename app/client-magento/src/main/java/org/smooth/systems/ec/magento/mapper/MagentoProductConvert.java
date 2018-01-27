package org.smooth.systems.ec.magento.mapper;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import org.smooth.systems.ec.migration.model.Product;
import org.smooth.systems.ec.migration.model.Product.ProductType;
import org.smooth.systems.ec.migration.model.Product.ProductVisibility;
import org.smooth.systems.ec.migration.model.ProductTranslateableAttributes;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class MagentoProductConvert {

  private DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
  // private SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd
  // HH:mm:ss");

  public Product convertCategory(com.github.chen0040.magento.models.Product product, String language) {
    Assert.notNull(product, "product is null, unable to convert");

    Product prod = new Product();
    prod.setSku(product.getSku());
    prod.setCostPrice(product.getPrice());
    prod.setCreationDate(LocalDateTime.parse(product.getUpdated_at(), DATE_FORMATTER));
    // prod.setSalesPrice(salesPrice);
    prod.setType(ProductType.Simple);
    prod.setVisibility(ProductVisibility.Everywhere);

    // prod.setAttributes(attributes);
    // prod.setCategories(categories);
    // prod.setDimension(dimension);
    // prod.setProductImageUrls(productImageUrls);

    populateProductAttributes(product, prod, language);
    // populateCategoryAttributes(category, cat, language);
    // populateChildCategory(cat, category.getChildren_data(), language);
    return prod;
  }

  private void populateProductAttributes(com.github.chen0040.magento.models.Product product, Product prod, String language) {
    ProductTranslateableAttributes attributes = new ProductTranslateableAttributes(language);
//    attributes.setDescription(description);
//    attributes.setFriendlyUrl(friendlyUrl);
//    attributes.setName(name);
//    attributes.setShortDescription(shortDescription);
//    attributes.setTags(tags);
    prod.setAttributes(attributes);
  }

  // private void populateChildProduct(Product parentCategory,
  // List<com.github.chen0040.magento.models.Category> subCategories,
  // String language) {
  // if (subCategories == null) {
  // return;
  // }
  // for (com.github.chen0040.magento.models.Category category : subCategories)
  // {
  // parentCategory.addSubCategory(convertCategory(category, language));
  // }
  // }
}
