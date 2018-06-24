package org.smooth.systems.ec.prestashop17.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.smooth.systems.ec.prestashop17.model.Category;
import org.smooth.systems.ec.prestashop17.model.ProductSpecificPrice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "prestashop")
@XmlAccessorType(XmlAccessType.FIELD)
class ProductSpecificPriceWrapper {

  @JsonProperty("specific_price")
  private ProductSpecificPrice specificPrice;
}
