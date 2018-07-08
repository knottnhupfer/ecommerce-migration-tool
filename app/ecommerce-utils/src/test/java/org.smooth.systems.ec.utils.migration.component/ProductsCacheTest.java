package org.smooth.systems.ec.utils.migration.component;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.smooth.systems.ec.client.api.ProductId;
import org.smooth.systems.ec.magento19.db.Magento19Constants;
import org.smooth.systems.ec.magento19.db.component.Magento19DbObjectReader;
import org.smooth.systems.ec.migration.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import lombok.extern.slf4j.Slf4j;

import static org.junit.Assert.assertEquals;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles({Magento19Constants.MAGENTO19_DB_PROFILE_NAME, "test"})
public class ProductsCacheTest {

  @Autowired
  private Magento19DbObjectReader reader;

  @Test
  public void retrieveOnlyActivatedProductsProductsCacheTest() {
    List<ProductId> productsList = Arrays.asList(createSimpleProduct(3718L), createSimpleProduct(3717L),
            createSimpleProduct(3704L), createSimpleProduct(3705L));
    ProductsCache.createProductsCache(reader, productsList, true);
    List<Product> retrievedProducts = reader.readAllProducts(productsList);
    Assert.notEmpty(retrievedProducts, "no retrieved products");
    assertEquals(2,retrievedProducts.size());
  }

  private ProductId createSimpleProduct(Long productId) {
    return createSimpleProduct(productId, "it");
  }

  private ProductId createSimpleProduct(Long productId, String langCode) {
    return ProductId.builder().productId(productId).langIso(langCode).build();
  }
}
