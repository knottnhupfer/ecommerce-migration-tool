package org.smooth.systems.ec.utils.migration.component;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.smooth.systems.ec.client.api.MigrationSystemReader;
import org.smooth.systems.ec.client.api.MigrationSystemWriter;
import org.smooth.systems.ec.component.MigrationSystemReaderAndWriterFactory;
import org.smooth.systems.ec.magento19.db.Magento19Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles({Magento19Constants.MAGENTO19_DB_PROFILE_NAME, "test"})
public class ReaderWriterMigrationTest {

  @Autowired
  private MigrationSystemReaderAndWriterFactory factory;

  private MigrationSystemReader reader;
  private MigrationSystemWriter writer;

  @PostConstruct
  public void init() {
    reader = factory.getMigrationReader();
    writer = factory.getMigrationWriter();
  }

  @Test
  public void testInjection() {

  }
}
