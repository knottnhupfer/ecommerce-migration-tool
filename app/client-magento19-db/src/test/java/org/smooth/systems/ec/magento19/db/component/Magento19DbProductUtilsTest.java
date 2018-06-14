package org.smooth.systems.ec.magento19.db.component;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.smooth.systems.ec.magento19.db.model.Magento19Product;
import org.smooth.systems.ec.magento19.db.model.Magento19ProductVisibility;
import org.smooth.systems.ec.magento19.db.repository.ProductRepository;
import org.smooth.systems.ec.magento19.db.repository.ProductVisibilityRepository;
import org.smooth.systems.ec.migration.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;
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

  @Test
  public void simpleRetrieveRootCategories() {
    assertNotNull(productFieldsProvider);
    Long brandIdForProduct = productFieldsProvider.getBrandIdForProduct(3717L);
    log.info("Product brand id: {}", brandIdForProduct);

    brandIdForProduct = productFieldsProvider.getBrandIdForProduct(78L);
    log.info("Product brand id: {}", brandIdForProduct);
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
  public void retrieveProductWuithMultipleCategoriesTest() {
    Product product;
    product = productRetriever.getProduct(2971L, "de");
    log.info("Product: {}", product);

    assertEquals(2,product.getCategories().size());
    assertTrue(product.getCategories().contains(new Long(11)));
    assertTrue(product.getCategories().contains(new Long(17)));
  }

  @Test
  public void productRetrieverTest() {
    Product product;
//    Product product = productRetriever.getProduct(2232L, "it");
//    log.info("Product: {}", product);

    product = productRetriever.getProduct(2971L, "de");
    log.info("Product: {}", product);

    product = productRetriever.getProduct(2169L, "de");
    log.info("Product: {}", product);

    product = productRetriever.getProduct(3717L, "it");
    log.info("Product: {}", product);

//    product = productRetriever.getProduct(78L, "it");
//    log.info("Product: {}", product);

    product = productRetriever.getProduct(2233L, "it");
    log.info("Product: {}", product);

//    product = productRetriever.getProduct(2049L, "de");
//    log.info("Product: {}", product);
  }

  @Test
  public void productMergingRetrieverTest() {
    List<Pair<Long, String>> asList = Arrays.asList(Pair.of(2302L, "it"), Pair.of(2784L, "de"));
    Product mergedProduct = productRetriever.getMergedProduct(asList);
    log.info("Merged product: {}", mergedProduct);

    asList = Arrays.asList(Pair.of(2498L, "it"), Pair.of(2785L, "de"));
    mergedProduct = productRetriever.getMergedProduct(asList);
    log.info("Merged product: {}", mergedProduct);
  }
}