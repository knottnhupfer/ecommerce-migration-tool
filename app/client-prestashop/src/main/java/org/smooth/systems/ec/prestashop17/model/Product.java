package org.smooth.systems.ec.prestashop17.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import org.smooth.systems.ec.exceptions.NotImplementedException;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Product {

  public enum Visibility {
    both;
  }

  @XmlElement(name = "id")
  private Long id;

//  @XmlElement(name = "categories")
//  private Long productId;
//  sfdfsd

  @XmlElement(name = "associations")
  private ProductAssociations associations;

  @XmlElement(name = "reference")
  private String sku;

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

  @XmlElement(name = "id_tax_rules_group")
  private Long taxRuleGroup = 1L;

  // TODO
  // http://prestashop.local/api/specific_prices
  // http://prestashop.local/api/specific_prices/1

  @JsonIgnore
  public void addCategoryId(Long categoryId) {
    throw new NotImplementedException();
  }

  @JsonIgnore
  public void addTagId(Long tagId) {
    throw new NotImplementedException();
  }
}