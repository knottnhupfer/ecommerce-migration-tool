package org.smooth.systems.ec.prestashop17.client;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.smooth.systems.ec.prestashop17.model.Manufacturer;
import org.springframework.util.Assert;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions> on 10.06.18.
 */
@Slf4j
public class Prestashop17ReadingClientTest {

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
}
