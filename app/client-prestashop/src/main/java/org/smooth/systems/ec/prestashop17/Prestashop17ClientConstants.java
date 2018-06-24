package org.smooth.systems.ec.prestashop17;

public interface Prestashop17ClientConstants {

  String URL_PRODUCT_IMAGE = "/images/products/%d";

  String URL_TAGS = "/tags";
  String URL_TAG = URL_TAGS + "/%d";

  String URL_PRODUCTS = "/products";
  String URL_PRODUCT = URL_PRODUCTS + "/%d";

  String URL_MANUFACTURERS = "/manufacturers";
  String URL_MANUFACTURER = URL_MANUFACTURERS + "/%d";

  String URL_STOCK_AVAILABLES = "/stock_availables";
  String URL_STOCK_AVAILABLE = URL_STOCK_AVAILABLES + "/%d";

  String URL_SPECIFIC_PRICES = "/specific_prices";
  String URL_SPECIFIC_PRICE = URL_SPECIFIC_PRICES + "/%d";

  static String getProductSpecificPriceUrl(Long specificPriceId) {
    return String.format(URL_SPECIFIC_PRICE, specificPriceId);
  }
}

//   /products/1021?price[my_price][use_tax]=1&price[my_price][product_attribute]=25