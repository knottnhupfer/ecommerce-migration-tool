package org.smooth.systems.ec.prestashop17.client;

import lombok.Data;
import org.smooth.systems.ec.prestashop17.model.ProductDescriptions;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "prestashop")
@XmlAccessorType(XmlAccessType.FIELD)
class ProductDescriptionsWrapper {

  @XmlElement(name = "product")
  private ProductDescriptions product;
}