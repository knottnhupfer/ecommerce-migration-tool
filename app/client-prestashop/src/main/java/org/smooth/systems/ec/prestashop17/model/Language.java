package org.smooth.systems.ec.prestashop17.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Language {

  @XmlElement(name = "id")
  private Long id;

  @XmlElement(name = "name")
  private String name;

  @XmlElement(name = "language_code")
  private String languageCode;
}