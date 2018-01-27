package org.smooth.systems.ec.prestashop17.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
class CategoryRef {

  @XmlAttribute(name = "id")
  private Long id;
}
