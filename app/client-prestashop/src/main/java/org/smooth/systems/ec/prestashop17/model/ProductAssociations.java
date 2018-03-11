package org.smooth.systems.ec.prestashop17.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductAssociations {

  @XmlElementWrapper(name = "categories")
  @XmlElement(name = "category")
  private List<ProductCategory> categories = new ArrayList<>();

  @XmlElementWrapper(name = "tags")
  @XmlElement(name = "tag")
  private List<ProductTag> tags = new ArrayList<>();
}