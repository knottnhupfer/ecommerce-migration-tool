package org.smooth.systems.ec.magento19.db.component;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.smooth.systems.ec.magento19.db.model.Magento19Product;
import org.smooth.systems.ec.magento19.db.model.Magento19ProductVisibility;
import org.smooth.systems.ec.magento19.db.repository.ProductDecimalRepository;
import org.smooth.systems.ec.magento19.db.repository.ProductRepository;
import org.smooth.systems.ec.magento19.db.repository.ProductVisibilityRepository;
import org.smooth.systems.ec.migration.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions> on
 * 09.02.18.
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class Magento19DbProductUtilsTest {

  @Autowired
  private ProductRepository productRepo;

  @Autowired
  private ProductVisibilityRepository productVisibilityRepo;

  @Autowired
  private Magento19DbProductFieldsProvider productFieldsProvider;

  @Autowired
  private Magento19DbProductsReader productRetriever;

  @Autowired
  private ProductDecimalRepository productDecimalRepo;

  @Test
  public void simpleRetrieveRootCategories() {
    assertNotNull(productFieldsProvider);
    Long brandIdForProduct = productFieldsProvider.getBrandIdForProduct(3717L);
  }

  @Test
  public void productRepositoryTest() {
    Magento19Product product = productRepo.findById(78L);
    log.info("Product: {}", product);

    product = productRepo.findById(3717L);
    log.info("Product: {}", product);
  }

  @Test
  public void productVisibilityRepositoryTest() {
    Magento19ProductVisibility visibility = productVisibilityRepo.findById(78L);
    log.info("Product visibility: {}", visibility);

    visibility = productVisibilityRepo.findById(3717L);
    log.info("Product visibility: {}", visibility);
  }

  @Test
  public void productPriceRetrieveTest() {
    Double salesPrice = productFieldsProvider.getProductSalesPrice(3717L);
    log.info("Sales price: {}", salesPrice);
    assertEquals(new Double("60.77"), salesPrice);

    salesPrice = productFieldsProvider.getProductSalesPrice(78L);
    log.info("Sales price: {}", salesPrice);
    assertEquals(new Double("31.30"), salesPrice);
  }

  @Test
  public void productWeightRetrieveTest() {
    Double weight = productFieldsProvider.getProductWeight(3717L);
    log.info("Weight: {}", weight);
    assertEquals(new Double("1.0"), weight);

    weight = productFieldsProvider.getProductWeight(78L);
    log.info("Weight: {}", weight);
    assertEquals(new Double("500.0"), weight);
  }

  @Test
  public void productRetrieverTest() {
    Product product = productRetriever.getProduct(3717L, "it");
    log.info("Product: {}", product);

    product = productRetriever.getProduct(78L, "it");
    log.info("Product: {}", product);
  }
}