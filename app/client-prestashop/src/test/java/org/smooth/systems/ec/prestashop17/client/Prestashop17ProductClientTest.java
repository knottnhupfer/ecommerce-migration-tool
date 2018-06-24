package org.smooth.systems.ec.prestashop17.client;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.smooth.systems.ec.prestashop17.model.Manufacturer;
import org.smooth.systems.ec.prestashop17.model.Product;
import org.smooth.systems.ec.prestashop17.model.ProductSpecificPrice;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions> on 10.06.18.
 */
@Slf4j
public class Prestashop17ProductClientTest {

  public static final Long EXISTING_CATEGORY_ID = 1L;

  private Prestashop17Client client;

  @Before
  public void setup() {
    client = Prestashop17TestConfiguration.createTestingClient();
  }

  @Test
  public void readAllTagsTest() {
    List<Manufacturer> manufacturers = client.getAllManufacturers();
    Assert.notNull(manufacturers, "manufacturers is null");
    log.info("Manufacturers: {}", manufacturers);
  }

  @Test
  public void getProductTest() {
    Product product = client.getProduct(PrestashopConstantsTests.EXISTING_PRODUCT_ID);
    log.info("Product: {}", product);
    log.info("ProductAssociations: {}", product.getAssociations());
  }

  @Test
  public void getProductSpecificPriceTest() {
    List<ProductSpecificPrice> productSpecificPrices = client.readAllProductSpecificPrices();
    log.info("ProductSpecificPrices: {}", productSpecificPrices);
  }
}
