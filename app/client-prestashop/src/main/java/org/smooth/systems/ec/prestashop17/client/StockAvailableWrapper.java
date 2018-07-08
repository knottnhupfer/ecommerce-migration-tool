package org.smooth.systems.ec.prestashop17.client;

import lombok.Data;
import org.smooth.systems.ec.prestashop17.model.Manufacturer;
import org.smooth.systems.ec.prestashop17.model.StockAvailable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions> on 12.06.18.
 */
@Data
@XmlRootElement(name = "prestashop")
@XmlAccessorType(XmlAccessType.FIELD)
public class StockAvailableWrapper {

  @XmlElement(name = "stock_available")
  private StockAvailable stockAvailable;
}
