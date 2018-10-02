package org.smooth.systems.ec.magento19.db.component;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.smooth.systems.ec.client.api.SimpleCategory;
import org.smooth.systems.ec.magento19.db.model.Magento19CategoryText;
import org.smooth.systems.ec.magento19.db.model.Magento19CategoryVarchar;
import org.smooth.systems.ec.magento19.db.model.Prestashop17DbStockAvailable;
import org.smooth.systems.ec.magento19.db.repository.CategoryTextRepository;
import org.smooth.systems.ec.magento19.db.repository.CategoryVarcharRepository;
import org.smooth.systems.ec.magento19.db.repository.Prestashop17DbStockAvailableRepository;
import org.smooth.systems.ec.migration.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertNotNull;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions> on 09.02.18.
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("prestashop")
public class Prestashop17DbTest {

  @Autowired
  private Prestashop17DbStockAvailableRepository prestashopRepo;

  @Test
  public void retrieveCategoryForIdTest() throws InterruptedException {
    assertNotNull(prestashopRepo);
    List<Prestashop17DbStockAvailable> all = prestashopRepo.findAll();
    log.info("stockAvailable: {}", all.size());


    Long shopId = 0L;
    Long shopGroupId = 1L;
    for(long prodId = 1; prodId < 1026; prodId++) {
//    for(long prodId = 60; prodId < 66; prodId++) {
      Prestashop17DbStockAvailable stockAvailable = new Prestashop17DbStockAvailable(prodId, shopId, shopGroupId);
      log.info("value: {}", stockAvailable);
      prestashopRepo.save(stockAvailable);
      Thread.sleep(100);
    }
  }
}