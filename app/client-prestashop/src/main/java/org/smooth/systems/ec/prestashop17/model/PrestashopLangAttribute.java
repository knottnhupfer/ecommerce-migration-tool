package org.smooth.systems.ec.prestashop17.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class PrestashopLangAttribute {

  private List<LanguageAttribute> values = new ArrayList<>();

  public void addAttribute(Long langId, String value) {
    values.add(new LanguageAttribute(langId, value));
  }
}
