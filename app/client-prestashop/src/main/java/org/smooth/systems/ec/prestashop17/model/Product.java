package org.smooth.systems.ec.prestashop17.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.util.Assert;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Product {

  public enum Visibility {
    both;
  }

  @XmlElement(name = "id")
  private Long id;

  @XmlElement(name = "low_stock_alert")
  private Long lowStockAlert = 0L;

  @XmlElement(name = "associations")
  private ProductAssociations associations = new ProductAssociations();

  @XmlElement(name = "reference")
  private String reference;

  @XmlElement(name = "weight")
  private String weight;

  @XmlElement(name = "id_manufacturer")
  private Long manufacturerId;

  // netto price
  @XmlElement(name = "price")
  private Double price;

  @XmlElement(name = "visibility")
  private Visibility visibility;

  @XmlElement(name = "name")
  private PrestashopLangAttribute names;

  @XmlElement(name = "description")
  private PrestashopLangAttribute descriptions;

  @XmlElement(name = "description_short")
  private PrestashopLangAttribute shortDescriptions;

  @XmlElement(name = "link_rewrite")
  private PrestashopLangAttribute friendlyUrls;

  /**
   * Initialized with default value
   */
  @XmlElement(name = "state")
  private Long state = 1L;

  @XmlElement(name = "active")
  private Long active = 1L;

  @XmlElement(name = "indexed")
  private Long indexed = 1L;

  @XmlElement(name = "show_price")
  private Long showPrice = 1L;

  @XmlElement(name = "id_tax_rules_group")
  private Long taxRuleGroup = 1L;

  // TODO
  // http://prestashop.local/api/specific_prices
  // http://prestashop.local/api/specific_prices/1

  @JsonIgnore
  public void addCategoryIds(List<Long> categoryIds) {
    categoryIds.forEach(categoryId -> {
      addCategoryId(categoryId);
    });
  }

  @JsonIgnore
  public void addCategoryId(Long categoryId) {
    Assert.notNull(categoryId, "categoryId is null");
    ProductCategory productCategory = new ProductCategory(categoryId);
    if (!associations.getCategories().contains(productCategory)) {
      associations.getCategories().add(productCategory);
    }
  }

  @JsonIgnore
  public void addTagId(Long tagId) {
    Assert.notNull(tagId, "tagId is null");
    ProductTag productTag = new ProductTag(tagId);
    if (!associations.getTags().contains(productTag)) {
      associations.getTags().add(productTag);
    }
  }
//
//  public static Product convertProduct(org.smooth.systems.ec.migration.model.Product product) {
//    Product prod = new Product();
//
//
//    product.getSalesPrice();
//    product.getRelatedProducts();
//
//    ;
//    product.getCreationDate();
//    product.getAttributes();
//    product.getCostPrice();
//
//    // skipped elements
////    product.getType();
//
//    // images will be uploaded in a special step
////    product.getProductImageUrls();
//
//
//    prod.setVisibility(convertVisibility(product.getVisibility()));
//    prod.setId(null);
//    prod.addCategoryIds(product.getCategories());
//    prod.setReference(product.getSku());
//    prod.setManufacturerId(product.getBrandId());
//
//    Double weight = product.getDimension().getWeight();
//    prod.setWeight(weight != null ? weight.toString() : "0.0");
//
//
//    prod.setDescriptions();
//    prod.setFriendlyUrls();
//    prod.setLowStockAlert();
//
//    prod.setNames();
//    prod.setPrice();
//    prod.setShortDescriptions();
//    prod.setManufacturerId();
//    return prod;
//  }
//
//  private static Visibility convertVisibility(org.smooth.systems.ec.migration.model.Product.ProductVisibility visibility) {
//    switch (visibility) {
//      case Catalog:
//      case Everywhere:
//      case NotVisible:
//      case Search:
//        return Visibility.both;
//    }
//    throw new IllegalStateException("Unknown type:" + visibility);
//  }
}