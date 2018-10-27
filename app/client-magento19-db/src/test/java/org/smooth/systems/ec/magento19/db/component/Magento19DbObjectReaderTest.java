package org.smooth.systems.ec.magento19.db.component;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.smooth.systems.ec.client.api.ObjectId;
import org.smooth.systems.ec.magento19.db.Magento19Constants;
import org.smooth.systems.ec.migration.model.Product;
import org.smooth.systems.ec.migration.model.ProductTranslateableAttributes;
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
public class Magento19DbObjectReaderTest {

  @Autowired
  private Magento19DbObjectReader reader;

  @Test
  public void retrieveProductFromDatabaseTest() {
//    List<ObjectId> productsList = Arrays.asList(ObjectId.builder().objectId(2233L).langIso("it").build());
    List<ObjectId> productsList = Arrays.asList(ObjectId.builder().objectId(2303L).langIso("it").build());

    List<Product> retrievedProducts = reader.readAllProducts(productsList);
    Assert.notEmpty(retrievedProducts, "no retrieved products");
    log.info("Product: {}", retrievedProducts.get(0).simpleDescription());
    log.info("");

//    productsList = Arrays.asList(ObjectId.builder().objectId(2971L).langIso("it").build());
//    retrievedProducts = reader.readAllProducts(productsList);
//    Assert.notEmpty(retrievedProducts, "no retrieved products");
//    log.info("Product: {}", retrievedProducts.get(0).simpleDescription());
//    log.info("");
  }

  @Test
  public void retrieveProductAttributesFromDatabaseTest() {
    List<ObjectId> productsList = Arrays.asList(ObjectId.builder().objectId(3718L).langIso("it").build());
    List<Product> retrievedProducts = reader.readAllProducts(productsList);
    Assert.notEmpty(retrievedProducts, "no retrieved products");
    Product product = retrievedProducts.get(0);
    assertEquals(Boolean.FALSE, product.getActivated());
    log.info("Product: {}", product.simpleDescription());
    log.info("");

    productsList = Arrays.asList(ObjectId.builder().objectId(3717L).langIso("it").build());
    retrievedProducts = reader.readAllProducts(productsList);
    Assert.notEmpty(retrievedProducts, "no retrieved products");
    product = retrievedProducts.get(0);
    assertEquals(Boolean.FALSE, product.getActivated());
    log.info("Product: {}", product.simpleDescription());
    log.info("");

    productsList = Arrays.asList(ObjectId.builder().objectId(3705L).langIso("it").build());
    retrievedProducts = reader.readAllProducts(productsList);
    Assert.notEmpty(retrievedProducts, "no retrieved products");
    product = retrievedProducts.get(0);
    assertEquals(Boolean.TRUE, product.getActivated());
    log.info("Product: {}", product.simpleDescription());
    log.info("");

    productsList = Arrays.asList(ObjectId.builder().objectId(3704L).langIso("it").build());
    retrievedProducts = reader.readAllProducts(productsList);
    Assert.notEmpty(retrievedProducts, "no retrieved products");
    product = retrievedProducts.get(0);
    assertEquals(Boolean.TRUE, product.getActivated());
    log.info("Product: {}", product.simpleDescription());
    log.info("");
  }

  @Test
  public void retrieveProductAndParseDescriptionTest() {
    List<ObjectId> productsList = Arrays.asList(ObjectId.builder().objectId(2140L).langIso("it").build());
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

    String desc = description.replaceAll("\r\n", "<br>");
    desc = desc.replaceAll("\n", "<br>");
    log.info("Product replaced: {}", desc);
    log.info("");
  }

	@Test
	public void retrieveRelatedProductsFromDatabaseTest() {
		List<ObjectId> productsList = Arrays.asList(ObjectId.builder().objectId(1642L).langIso("it").build());
		List<Product> retrievedProducts = reader.readAllProducts(productsList);
		System.out.println("Product: " +  retrievedProducts.get(0).toString());
	}

	@Test
	public void retrieveProductsBySkuFromDatabaseTest() {
		List<String> skus = Arrays.asList(
			"L5106", "L693S3E", "L691S3E", "L691R2E", "L692S3E", "L692R2E", "L69DS3E", "L69CS3E", "L690S3E", "L690S1E",
			"L690R2E", "L690001", "L69BS3E", "L69BS1E", "L69BR2E"
		);

		long index = 1;
		for(String sku : skus) {

			Product productItalian = retrieveProductForSku(sku);
			ProductTranslateableAttributes attributes = productItalian.getAttributes().get(0);
			System.out.println("\n" + sku + "\n" + attributes.getName());

			Product productGerman = retrieveProductForSku("_" + sku);
			if(productGerman != null) {
				ProductTranslateableAttributes attributesGerman = productGerman.getAttributes().get(0);
				System.out.println(attributesGerman.getName());
			}
			if(index++ % 5 == 0) {
				System.out.println("\n ==========     ==========");
			}
		}
		System.out.println("\n ==========     ==========\n");
	}

	private Product retrieveProductForSku(String sku) {
  	try {
			return reader.readProductBySku(sku, "de");
		} catch(IllegalArgumentException e) {
			return null;
		}
	}
}
