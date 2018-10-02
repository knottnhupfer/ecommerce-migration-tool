package org.smooth.systems.ec.prestashop17.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

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

  @XmlElement(name = "additional_delivery_times")
  private Long deliveryType = 1L;

  @XmlElement(name = "associations")
  private ProductAssociations associations = new ProductAssociations();

  @XmlElement(name = "reference")
  private String reference;

  @XmlElement(name = "weight")
  private String weight;

  @XmlElement(name = "id_manufacturer")
  private Long manufacturerId;

  @XmlElement(name = "id_category_default")
  private Long defaultCategoryId = null;

  @XmlElement(name = "price")
  private Double netPrice;

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

  @XmlElement(name = "redirect_type")
  private String redirectType = "404";

  @XmlElement(name = "available_for_order")
  private Long availableForOrder = 1L;

  @XmlElement(name = "minimal_quantity")
  private Long minimalQuantity = 1L;

  @JsonIgnore
  public void addCategoryIds(List<Long> categoryIds) {
    if(defaultCategoryId == null && !ObjectUtils.isEmpty(categoryIds)) {
      defaultCategoryId = categoryIds.get(0);
    }
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
}