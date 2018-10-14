package org.smooth.systems.ec.prestashop17.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.List;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Product {

  public enum Visibility {
    both;
  }

  @JsonProperty("id")
  private Long id;

  @JsonProperty("low_stock_alert")
  private Long lowStockAlert = 0L;

  @JsonProperty("additional_delivery_times")
  private Long deliveryType = 1L;

  @JsonProperty("associations")
  private ProductAssociations associations = new ProductAssociations();

  @JsonProperty("reference")
  private String reference;

  @JsonProperty("weight")
  private String weight;

  @JsonProperty("id_manufacturer")
  private Long manufacturerId;

  @JsonProperty("id_category_default")
  private Long defaultCategoryId = null;

  @JsonProperty("price")
  private Double netPrice;

  @JsonProperty("visibility")
  private Visibility visibility;

	@JsonProperty("name")
	private LanguageContainer names;

	@JsonProperty("description")
	private LanguageContainer descriptions;

	@JsonProperty("description_short")
	private LanguageContainer shortDescriptions;

	@JsonProperty("link_rewrite")
	private LanguageContainer friendlyUrls;

  /**
   * Initialized with default value
   */
  @JsonProperty("state")
  private Long state = 1L;

  @JsonProperty("active")
  private Long active = 1L;

  @JsonProperty("indexed")
  private Long indexed = 1L;

  @JsonProperty("show_price")
  private Long showPrice = 1L;

  @JsonProperty("id_tax_rules_group")
  private Long taxRuleGroup = 1L;

  @JsonProperty("redirect_type")
  private String redirectType = "404";

  @JsonProperty("available_for_order")
  private Long availableForOrder = 1L;

  @JsonProperty("minimal_quantity")
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