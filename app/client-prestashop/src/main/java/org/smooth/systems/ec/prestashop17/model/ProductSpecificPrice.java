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

  @JsonProperty("id_shop")
  private Long shopId;

  @JsonProperty("id_cart")
  private Long cartId = 0L;

  // TODO currently not supported
  @JsonProperty("id_currency")
  private Long currencyId = 0L;

  @JsonProperty("id_country")
  private Long countryId = 0L;

  @JsonProperty("id_group")
  private Long groupId = 0L;

  @JsonProperty("id_customer")
  private Long customerId = 0L;

  @JsonProperty("price")
  private String price = "-1.000000";

  @JsonProperty("from")
  private String from = "0000-00-00 00:00:00";

  @JsonProperty("to")
  private String to = "0000-00-00 00:00:00";
}
