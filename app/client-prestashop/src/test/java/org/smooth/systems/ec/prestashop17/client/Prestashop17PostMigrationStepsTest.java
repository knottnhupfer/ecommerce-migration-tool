package org.smooth.systems.ec.prestashop17.client;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.smooth.systems.ec.migration.testing.Prestashop17WebserviceTests;
import org.smooth.systems.ec.prestashop17.model.Manufacturer;
import org.smooth.systems.ec.prestashop17.model.Product;
import org.smooth.systems.ec.prestashop17.model.ProductSpecificPrice;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions> on 10.06.18.
 */
@Slf4j
@Category(Prestashop17WebserviceTests.class)
public class Prestashop17PostMigrationStepsTest {

  public static final Long EXISTING_CATEGORY_ID = 1L;

  private Prestashop17Client client;

  @Before
  public void setup() {
    client = Prestashop17TestConfiguration.createTestingClient();
  }

  @Test
  public void rewriteAllStockAvailable() throws InterruptedException {
    Long shopId = 1L;
    Long shopGroupId = 0L;
//    List<ProductRef> productsMetaData = client.getProductsMetaData();
//    log.info("Add new stock availability for {} products.", productsMetaData.size());
//
////    productsMetaData = productsMetaData.subList(0,5);
////    log.info("Add new stock availability for {} products.", productsMetaData.size());
//    for(ProductRef prodRef : productsMetaData) {
//      Thread.sleep(200);
//      client.enableIgnoreStock(prodRef.getId(),shopId, shopGroupId);
//    }

    for (long i = 1; i < 1026; i++) {
      Thread.sleep(250);
      client.enableIgnoreStock(null, i, shopId, shopGroupId);
    }
  }

//    @Test
//    public void createAdditionaltockAvailable() throws InterruptedException {
//      Long shopId = 0L;
//      Long shopGroupId = 1L;
////    List<ProductRef> productsMetaData = client.getProductsMetaData();
////    log.info("Add new stock availability for {} products.", productsMetaData.size());
////
//////    productsMetaData = productsMetaData.subList(0,5);
//////    log.info("Add new stock availability for {} products.", productsMetaData.size());
////    for(ProductRef prodRef : productsMetaData) {
////      Thread.sleep(200);
////      client.enableIgnoreStock(prodRef.getId(),shopId, shopGroupId);
////    }
//
////    for(long i = 1; i < 1026; i++) {
//      for(long i = 1; i < 2; i++) {
//        Thread.sleep(250);
//        client.enableIgnoreStock(null,i,shopId, shopGroupId);
//      }
//  }
}
