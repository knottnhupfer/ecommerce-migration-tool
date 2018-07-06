package org.smooth.systems.ec.prestashop17.client;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
class ProductRef {

  @XmlAttribute(name = "id")
  private Long id;
}
