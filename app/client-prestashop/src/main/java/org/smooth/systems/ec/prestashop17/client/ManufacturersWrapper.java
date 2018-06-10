package org.smooth.systems.ec.prestashop17.client;

import lombok.Data;
import org.smooth.systems.ec.prestashop17.model.Manufacturer;
import org.smooth.systems.ec.prestashop17.model.Tag;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "prestashop")
@XmlAccessorType(XmlAccessType.FIELD)
class ManufacturersWrapper {

  @XmlElement(name = "manufacturer")
  private Manufacturer manufacturer;
}
