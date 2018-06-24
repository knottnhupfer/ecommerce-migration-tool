package org.smooth.systems.ec.prestashop17.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

@Data
@XmlRootElement(name = "prestashop")
@XmlAccessorType(XmlAccessType.FIELD)
class ProductSpecificPrices {

  @JsonProperty("specific_prices")
  private List<ObjectRefId> specificPrices;
}
