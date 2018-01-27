package org.smooth.systems.ec.prestashop17.client;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

@Data
@XmlRootElement(name = "prestashop")
@XmlAccessorType(XmlAccessType.FIELD)
class Categories {

  @XmlElement(name = "categories")
  private CategoriesWrapper wrapper;

  @Data
  @XmlAccessorType(XmlAccessType.FIELD)
  static class CategoriesWrapper {

    @XmlElement(name = "category")
    private List<CategoryRef> categoryRefs;
  }
}