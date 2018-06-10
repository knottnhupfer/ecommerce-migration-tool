package org.smooth.systems.ec.prestashop17.client;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

@Data
@XmlRootElement(name = "prestashop")
@XmlAccessorType(XmlAccessType.FIELD)
class Manufacturers {

  @XmlElementWrapper(name = "manufacturers")
  @XmlElement(name = "manufacturer")
  private List<ObjectRefId> manufacturers;
}