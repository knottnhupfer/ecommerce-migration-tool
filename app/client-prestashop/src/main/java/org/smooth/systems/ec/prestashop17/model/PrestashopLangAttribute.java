package org.smooth.systems.ec.prestashop17.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class PrestashopLangAttribute {

  @XmlElement(name = "language")
  private List<LanguageAttribute> values = new ArrayList<>();

  public void addAttribute(Long langId, String value) {
    LanguageAttribute attr = new LanguageAttribute();
    attr.setId(langId);
    attr.setValue(value);
    values.add(attr);
  }
}
