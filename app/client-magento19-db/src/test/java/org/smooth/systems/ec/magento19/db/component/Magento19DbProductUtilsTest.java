package org.smooth.systems.ec.magento19.db.component;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions> on 09.02.18.
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class Magento19DbProductUtilsTest {

	@Autowired
	private Magento19DbProductFieldsProvider productFieldsProvider;

	@Test
	public void simpleRetrieveRootCategories() {
		assertNotNull(productFieldsProvider);
		String brandName = productFieldsProvider.getBrandIdForProduct(3717L);
		assertNull(brandName);
		brandName = productFieldsProvider.getBrandIdForProduct(78L);
    assertEquals("Mean Well", brandName);
		log.info("Brand name: {}", brandName);
	}
}