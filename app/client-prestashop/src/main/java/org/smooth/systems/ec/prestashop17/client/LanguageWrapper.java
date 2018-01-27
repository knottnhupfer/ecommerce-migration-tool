package org.smooth.systems.ec.prestashop17.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.smooth.systems.ec.prestashop17.model.Language;

import lombok.Data;

@Data
@XmlRootElement(name = "prestashop")
@XmlAccessorType(XmlAccessType.FIELD)
class LanguageWrapper {

  @XmlElement(name = "language")
  private Language language;
}
