package org.smooth.systems.ec.client.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@UtilityClass
public class PriceConvertUtil {

  // TODO default values currently hard coded and price calculation
  public static final int DEFAULT_ROUND_PLACES = 2;

  // TODO default values currently hard coded and price calculation
  public static Double DEFAULT_TAX_RATE = new Double("1.22");

  public double round(double value) {
    return round(new BigDecimal(value)).doubleValue();
  }

  public BigDecimal round(BigDecimal value) {
    return value.setScale(DEFAULT_ROUND_PLACES, RoundingMode.HALF_UP);
  }

  public Double convertGrossPriceToNetPrice(Double grossPrice) {
    BigDecimal netPrice = BigDecimal.valueOf(grossPrice).divide(BigDecimal.valueOf(DEFAULT_TAX_RATE), 6, RoundingMode.HALF_UP);
    return round(netPrice).doubleValue();
  }
}
