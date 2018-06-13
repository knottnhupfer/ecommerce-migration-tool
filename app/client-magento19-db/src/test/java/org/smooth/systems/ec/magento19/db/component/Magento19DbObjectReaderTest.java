package org.smooth.systems.ec.magento19.db.component;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.smooth.systems.ec.client.api.SimpleProduct;
import org.smooth.systems.ec.magento19.db.Magento19Constants;
import org.smooth.systems.ec.migration.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles({ Magento19Constants.MAGENTO19_DB_PROFILE_NAME, "test" })
public class Magento19DbObjectReaderTest {

  @Autowired
  private Magento19DbObjectReader reader;

  @Test
  public void retrieveProductFromDatabaseTest() {
    List<SimpleProduct> productsList = Arrays.asList(SimpleProduct.builder().productId(2971L).langIso("it").build());
    List<Product> retrievedProducts = reader.readAllProducts(productsList);
    Assert.notEmpty(retrievedProducts, "no retrieved products");
    log.info("Product: {}", retrievedProducts.get(0).simpleDescription());
    log.info("");

    productsList = Arrays.asList(SimpleProduct.builder().productId(2971L).langIso("it").build());
    retrievedProducts = reader.readAllProducts(productsList);
    Assert.notEmpty(retrievedProducts, "no retrieved products");
    log.info("Product: {}", retrievedProducts.get(0));
    log.info("");
  }

  @Test
  public void retrieveProductAndParseDescriptionTest() {
    List<SimpleProduct> productsList = Arrays.asList(SimpleProduct.builder().productId(2140L).langIso("it").build());
    List<Product> retrievedProducts = reader.readAllProducts(productsList);
    Assert.notEmpty(retrievedProducts, "no retrieved products");
    Product product = retrievedProducts.get(0);
    String description = product.getAttributes().get(0).getDescription();
    log.info("Product description: {}", description);
    log.info("");

//    int i1 = description.indexOf("Classe elettrica");
//    int i2 = description.indexOf("DURATA");
//    String substring = description.substring(i1, i1 + 50);
//    log.info("Product substring: {}", substring);
//    log.info("");

    String desc = description.replaceAll("\r\n","<br>");
    desc = desc.replaceAll("\n","<br>");
    log.info("Product replaced: {}", desc);
    log.info("");
  }
}
