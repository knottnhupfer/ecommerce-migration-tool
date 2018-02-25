package org.smooth.systems.ec.magento19.db.component;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.smooth.systems.ec.exceptions.NotImplementedException;
import org.smooth.systems.ec.magento19.db.model.Magento19Product;
import org.smooth.systems.ec.magento19.db.repository.ProductRepository;
import org.smooth.systems.ec.migration.model.Product;
import org.smooth.systems.ec.migration.model.Product.ProductType;
import org.smooth.systems.ec.migration.model.Product.ProductVisibility;
import org.smooth.systems.ec.migration.model.ProductDimensionAndShipping;
import org.smooth.systems.ec.migration.model.ProductTranslateableAttributes;
import org.springframework.beans.factory.annotation.Autowired;
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

  public Product getProduct(Long productId, String langCode) {
    Assert.notNull(productId, "productId is null");

    Magento19Product retrievedProduct = productRepo.findById(productId);
    LocalDateTime creationDate = LocalDateTime.ofInstant(retrievedProduct.getCreatedAt().toInstant(), ZoneId.systemDefault());
    Product product = new Product(retrievedProduct.getId(), retrievedProduct.getSku(), creationDate, ProductType.Simple);

    updateProductPrice(product);
    updateMainCategoryId(product);
    updateProductVisibility(product);
    updateProductManufacturer(product);
    updateDimensionAndShippingForProduct(product);

    // product.getAttributes().add(getTranslateablAttributesForProduct(productId,
    // langCode));
    return product;
  }

  public ProductTranslateableAttributes getTranslateablAttributesForProduct(Long productId, String langCode) {
    // private List<ProductTranslateableAttributes> attributes = new
    // ArrayList<>();
    throw new NotImplementedException();
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

  private Long getId(Product product) {
    Assert.notNull(product.getId(), "productId is null");
    return product.getId();
  }

  private void logRetrievedValue(String valueName, Object value, Product product) {
    log.trace("Retrieved product {}: {} for productId: {}", valueName, value, product.getId());
  }
}

// private List<String> productImageUrls = new ArrayList<>();