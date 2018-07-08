package org.smooth.systems.ec.prestashop17.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions> on 12.06.18.
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class StockAvailable {

  @XmlElement(name = "id")
  private Long id;

  @XmlElement(name = "id_product")
  private Long productId;

  @XmlElement(name = "id_product_attribute")
  private Long productAttributeId = 0L;

  @XmlElement(name = "id_shop")
  private Long shopId = 1L;

  @XmlElement(name = "id_shop_group")
  private Long shopGroupId = 0L;

  @XmlElement(name = "quantity")
  private Long quantity = 0L;

  @XmlElement(name = "depends_on_stock")
  private Long dependsOnStock = 0L;

  @XmlElement(name = "out_of_stock")
  private Long outOfStock;
}
