package org.smooth.systems.ec.prestashop17.model;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.smooth.systems.ec.client.util.PriceConvertUtil;
import org.smooth.systems.ec.exceptions.NotImplementedException;
import org.smooth.systems.ec.migration.model.ProductTierPriceStrategy;
import org.smooth.systems.ec.migration.model.ProductTranslateableAttributes;
import org.smooth.systems.ec.prestashop17.component.PrestashopLanguageTranslatorCache;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;

import static org.smooth.systems.ec.client.util.PriceConvertUtil.DEFAULT_ROUND_PLACES;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions>
 */
@Slf4j
@UtilityClass
public class ProductConvertUtil {

  public static final Long DEFAULT_TAX_GROUP = 5L;

//  public static final int DEFAULT_ROUND_PLACES = 2;

  public Product convertProduct(PrestashopLanguageTranslatorCache cache, org.smooth.systems.ec.migration.model.Product product) {
    Product prod = new Product();

    // skipped elements
//    product.getType();
//    product.getCreationDate();
//    product.getSalesPrice();
//    product.getRelatedProducts();

    // images will be uploaded in a special step
//    product.getProductImageUrls();

    prod.setVisibility(convertVisibility(product.getVisibility()));
    prod.setId(null);
    prod.addCategoryIds(product.getCategories());
    prod.setReference(product.getSku());
    prod.setManufacturerId(product.getBrandId());

    Double weight = product.getDimension().getWeight();
    prod.setWeight(weight != null ? weight.toString() : "0.0");

    List<ProductTranslateableAttributes> attributes = product.getAttributes();
    prod.setNames(retrieveNames(cache, attributes));
    prod.setDescriptions(retrieveDescriptions(cache, attributes));
    prod.setFriendlyUrls(retrieveLinksRewrite(cache, attributes));
    prod.setShortDescriptions(retrieveShortDescriptions(cache, attributes));

    prod.setTaxRuleGroup(DEFAULT_TAX_GROUP);
    prod.setNetPrice(product.getNetPrice());
    Assert.notNull(prod.getNetPrice(), "prestashop 17 product net price is null");
    return prod;
  }

  private Product.Visibility convertVisibility(org.smooth.systems.ec.migration.model.Product.ProductVisibility visibility) {
    switch (visibility) {
      case Catalog:
      case Everywhere:
      case NotVisible:
      case Search:
        return Product.Visibility.both;
    }
    throw new IllegalStateException("Unknown type:" + visibility);
  }

  private PrestashopLangAttribute retrieveNames(PrestashopLanguageTranslatorCache cache, List<ProductTranslateableAttributes> attributes) {
    PrestashopLangAttribute attrs = new PrestashopLangAttribute();
    attributes.forEach(attr -> {
      attrs.addAttribute(cache.getLangId(attr.getLangCode()), attr.getName());
    });
    return attrs;
  }

  private PrestashopLangAttribute retrieveDescriptions(PrestashopLanguageTranslatorCache cache, List<ProductTranslateableAttributes> attributes) {
    PrestashopLangAttribute attrs = new PrestashopLangAttribute();
    attributes.forEach(attr -> {
      attrs.addAttribute(cache.getLangId(attr.getLangCode()), attr.getDescription());
    });
    return attrs;
  }

  private PrestashopLangAttribute retrieveShortDescriptions(PrestashopLanguageTranslatorCache cache, List<ProductTranslateableAttributes> attributes) {
    PrestashopLangAttribute attrs = new PrestashopLangAttribute();
    attributes.forEach(attr -> {
      attrs.addAttribute(cache.getLangId(attr.getLangCode()), attr.getShortDescription());
    });
    return attrs;
  }

  private PrestashopLangAttribute retrieveLinksRewrite(PrestashopLanguageTranslatorCache cache, List<ProductTranslateableAttributes> attributes) {
    PrestashopLangAttribute attrs = new PrestashopLangAttribute();
    attributes.forEach(attr -> {
      attrs.addAttribute(cache.getLangId(attr.getLangCode()), attr.getFriendlyUrl());
    });
    return attrs;
  }

  public static ProductSpecificPrice convertProductPriceStrategy(Double fullNetPrice, Long productId, ProductTierPriceStrategy priceStrategy) {
    ProductSpecificPrice specificPrice = new ProductSpecificPrice();

    // TODO test with single product from MagentoDb19
    // TODO fix this, currently it works for default shops
    // must be retrieved and implemented somewhere else
    specificPrice.setShopId(1L);

    specificPrice.setProductId(productId);
    specificPrice.setQuantity(priceStrategy.getMinQuantity());

    specificPrice.setReductionTax(ProductSpecificPrice.EXCLUSIVE_TAX);
    if (priceStrategy.getDiscountType() == ProductTierPriceStrategy.DiscountType.PRICE) {
      specificPrice.setReductionType(ProductSpecificPrice.REDUCTION_TYPE_AMOUNT);
      Double priceReduction = calculateNetPriceReduction(fullNetPrice, priceStrategy.getValue());
      specificPrice.setReduction(priceReduction);
    } else {
      specificPrice.setReductionType(ProductSpecificPrice.REDUCTION_TYPE_PERCENTAGE);
      specificPrice.setReduction(priceStrategy.getValue());
    }
    return specificPrice;
  }

  /**
   *
   * @param fullNetPrice
   * @param reducedNetPrice
   * @return net price reduction; difference between full net price and reduced net price
   */
  private static Double calculateNetPriceReduction(Double fullNetPrice, Double reducedNetPrice) {
    BigDecimal fullNetPricePrecise = BigDecimal.valueOf(fullNetPrice);
    BigDecimal reducedNetPricePrecise = BigDecimal.valueOf(reducedNetPrice);
    if(reducedNetPricePrecise.compareTo(fullNetPricePrecise) >= 0) {
      log.error("Invalid price reduction, reduced net price equal or higher then the full net price");
      log.error("Comparing result: {}, reduced net price: {}, full net price: {}", reducedNetPricePrecise.compareTo(fullNetPricePrecise),
              reducedNetPricePrecise, fullNetPricePrecise);
      return reducedNetPrice;
//      throw new IllegalStateException("Invalid price reduction, reduced net price equal or higher then the full net price");
    }
    return PriceConvertUtil.round(fullNetPricePrecise.subtract(reducedNetPricePrecise)).doubleValue();
  }
}
