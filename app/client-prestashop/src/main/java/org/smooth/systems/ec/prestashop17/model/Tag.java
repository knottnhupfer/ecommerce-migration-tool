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
public class Tag {

  @XmlElement(name = "id")
  private Long id;

  @XmlElement(name = "name")
  private String name;

  @XmlElement(name = "id_lang")
  private Long langId;

  public Tag(Long languageId, String name) {
    this.name = name;
    this.langId = languageId;
  }
}