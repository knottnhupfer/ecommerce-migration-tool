package org.smooth.systems.ec.prestashop17.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.xml.bind.annotation.*;

@Data
@XmlRootElement(name = "specific_price")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown =  true)
public class ProductSpecificPrice {

  public static final Long EXCLUSIVE_TAX = 0L;
  public static final Long INCLUSIVE_TAX = 1L;

  public static final String REDUCTION_TYPE_AMOUNT = "amount";
  public static final String REDUCTION_TYPE_PERCENTAGE = "percentage";

  @JsonProperty("from_quantity")
  private Long quantity;

  // price or percentage
  @JsonProperty("reduction")
  private Double reduction;

  @JsonProperty("reduction_tax")
  private Long reductionTax;

  @JsonProperty("reduction_type")
  private String reductionType;

  @JsonProperty("id_product")
  private Long productId;
}
