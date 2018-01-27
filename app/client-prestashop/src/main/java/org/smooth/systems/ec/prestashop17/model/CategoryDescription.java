package org.smooth.systems.ec.prestashop17.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class CategoryDescription {

  @XmlElement(name = "language")
  private List<LanguageAttribute> names;
}
