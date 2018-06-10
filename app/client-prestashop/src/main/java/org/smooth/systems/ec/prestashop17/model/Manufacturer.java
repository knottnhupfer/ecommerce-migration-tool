package org.smooth.systems.ec.prestashop17.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class Manufacturer {

  private Long id;

  private Long active = new Long(1);

  private String name;

  @XmlElement(name = "description")
  private PrestashopLangAttribute descriptions;

  @XmlElement(name = "short_description")
  private PrestashopLangAttribute shortDescriptions;

  public org.smooth.systems.ec.migration.model.Manufacturer convert() {
    org.smooth.systems.ec.migration.model.Manufacturer manufacturer = new org.smooth.systems.ec.migration.model.Manufacturer();
    manufacturer.setId(id);
    manufacturer.setName(name);
    return manufacturer;
  }
}