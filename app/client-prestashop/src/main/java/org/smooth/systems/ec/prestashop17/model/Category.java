package org.smooth.systems.ec.prestashop17.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Category {

  @XmlElement(name = "id")
  private Long id;

  @XmlElement(name = "id_parent")
  private Long parentId;

  @XmlElement(name = "is_root_category")
  private Long isRootCategory;

  @XmlElement(name = "active")
  private Long active;

  @XmlElement(name = "name")
  private PrestashopLangAttribute names;

  @XmlElement(name = "description")
  private PrestashopLangAttribute descriptions;

  @XmlElement(name = "link_rewrite")
  private PrestashopLangAttribute friendlyUrls;
}