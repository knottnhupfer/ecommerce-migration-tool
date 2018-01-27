package org.smooth.systems.ec.prestashop17.client;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

@Data
@XmlRootElement(name = "prestashop")
@XmlAccessorType(XmlAccessType.FIELD)
class Languages {

  @XmlElement(name = "languages")
  private LanguagesWrapper wrapper;

  @Data
  @XmlAccessorType(XmlAccessType.FIELD)
  static class LanguagesWrapper {
    
    @XmlElement(name = "language")
    private List<LanguageRef> languages;
  }
}