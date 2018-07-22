package org.smooth.systems.ec.utils.migration.component;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.smooth.systems.ec.client.api.MigrationSystemReader;
import org.smooth.systems.ec.client.api.MigrationSystemWriter;
import org.smooth.systems.ec.component.MigrationSystemReaderAndWriterFactory;
import org.smooth.systems.ec.magento19.db.Magento19Constants;
import org.smooth.systems.ec.migration.model.IProductMetaData;
import org.smooth.systems.ec.utils.EcommerceUtilRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles({Magento19Constants.MAGENTO19_DB_PROFILE_NAME, "test"})
@ComponentScan(basePackages = {"org.smooth.systems.ec"}, excludeFilters={
        @ComponentScan.Filter(type=FilterType.ASSIGNABLE_TYPE, value=EcommerceUtilRunner.class)})
public class ReaderWriterMigrationTest {

  @Autowired
  private MigrationSystemReaderAndWriterFactory factory;

  private MigrationSystemReader reader;
  private MigrationSystemWriter writer;

  @PostConstruct
  public void init() {
    reader = factory.getMigrationReader("prestashop17");
    writer = factory.getMigrationWriter("prestashop17");
  }

  @Test
  public void removeAllProductSpecificPrices() {
    List<Long> productSpecificPricesIds = reader.readAllProductSpecificPrices();
  }
}
