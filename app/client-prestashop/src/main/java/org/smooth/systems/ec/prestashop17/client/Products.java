package org.smooth.systems.ec.prestashop17.client;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Data
@XmlRootElement(name = "prestashop")
@XmlAccessorType(XmlAccessType.FIELD)
class Products {

  @XmlElement(name = "products")
  private CategoriesWrapper wrapper;

  @Data
  @XmlAccessorType(XmlAccessType.FIELD)
  static class CategoriesWrapper {

    @XmlElement(name = "product")
    private List<ProductRef> productRefs;
  }
}