package org.smooth.systems.ec.prestashop17.client;

import java.util.List;

import javax.xml.bind.annotation.*;

import lombok.Data;

@Data
@XmlRootElement(name = "prestashop")
@XmlAccessorType(XmlAccessType.FIELD)
class Categories {

  @XmlElementWrapper(name = "categories")
  @XmlElement(name = "category")
  private List<ObjectRefId> categories;
}