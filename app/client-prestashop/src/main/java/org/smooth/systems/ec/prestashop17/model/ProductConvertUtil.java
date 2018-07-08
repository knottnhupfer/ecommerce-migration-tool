package org.smooth.systems.ec.prestashop17.model;

import lombok.experimental.UtilityClass;
import org.smooth.systems.ec.exceptions.NotImplementedException;
import org.smooth.systems.ec.migration.model.ProductTierPriceStrategy;
import org.smooth.systems.ec.migration.model.ProductTranslateableAttributes;
import org.smooth.systems.ec.prestashop17.component.PrestashopLanguageTranslatorCache;

import java.util.List;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions> on 11.06.18.
 */
@UtilityClass
public class ProductConvertUtil {

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
    prod.setPrice(product.getCostPrice());
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

  public static ProductSpecificPrice convertProductPriceStrategy(Long productId, ProductTierPriceStrategy priceStrategy) {
    ProductSpecificPrice specificPrice = new ProductSpecificPrice();
    specificPrice.setProductId(productId);
    specificPrice.setQuantity(priceStrategy.getMinQuantity());
    specificPrice.setReduction(priceStrategy.getValue());
    if (priceStrategy.getDiscountType() == ProductTierPriceStrategy.DiscountType.PRICE) {
      specificPrice.setReductionType(ProductSpecificPrice.REDUCTION_TYPE_AMOUNT);
    } else {
      specificPrice.setReductionType(ProductSpecificPrice.REDUCTION_TYPE_AMOUNT);
    }
    specificPrice.setReductionTax(priceStrategy.isDiscountTaxIncluded() ? ProductSpecificPrice.INCLUSIVE_TAX : ProductSpecificPrice.EXCLUSIVE_TAX);

    // TODO test with single product from MagentoDb19
    // TODO fix this, currently it works for default shops
    // must be retrieved and implemented somewhere else
    specificPrice.setShopId(1L);
    throw new NotImplementedException();
//    return specificPrice;
  }
}
