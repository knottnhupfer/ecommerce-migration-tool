package org.smooth.systems.ec.prestashop17.db;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.smooth.systems.ec.prestashop17.db.repository.Prestashop17DbStockAvailableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNotNull;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions>
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest
@EnableJpaRepositories
@EntityScan("org.smooth.systems.ec.prestashop17.db.model")
//@SpringBootConfiguration
//@ComponentScan({"org.smooth.systems.ec"})
@SpringBootTest(classes=Prestashop17DbStockAvailableRepositoryTest.class)
public class Prestashop17DbStockAvailableRepositoryTest {

	@Autowired
	private Prestashop17DbStockAvailableRepository stockAvaiableRepo;

	@Test
	public void simpleRetrieveRootCategories() {
		assertNotNull(stockAvaiableRepo);

	}
}