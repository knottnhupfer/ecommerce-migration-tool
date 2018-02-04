package org.smooth.systems.magento.client;

import java.util.List;

import org.junit.Test;
import org.smooth.systems.ec.magento.AbstractMagentoConnector;
import org.smooth.systems.ec.magento.Magento2ConnectorConfiguration;
import org.smooth.systems.ec.magento.mapper.MagentoProductConvert;

import com.github.chen0040.magento.MagentoClient;
import com.github.chen0040.magento.models.Product;
import com.github.chen0040.magento.models.ProductPage;

public class Magento2RetrieveProductsTest extends AbstractMagentoConnector {

  private static final String MAGENTO_HOSTNAME = "http://data-migration-magento.local/index.php";

  private static final String MAGENTO_USER = "admin";

  private static final String MAGENTO_PASSWD = "secret123";

  private static Magento2ConnectorConfiguration MAGENTO_CONFIG = new Magento2ConnectorConfiguration(MAGENTO_HOSTNAME, MAGENTO_USER,
      MAGENTO_PASSWD);

  public Magento2RetrieveProductsTest() {
    super(MAGENTO_CONFIG);
  }

  
  // bugfix for retrieving products for shop
  // https://github.com/magento/magento2/issues/3864
  
  @Test
  public void simpleConnection() {
    MagentoClient client = getClient();

    int pageIndex = 0;
//    int pageSize = 20;
    int pageSize = 20;
    ProductPage page = client.products().page(pageIndex, pageSize);
    List<Product> products = page.getItems();
    System.out.println("Products: #" + products.size());

//    page = client.products().page("led_base_view", pageIndex, pageSize);
//    products = page.getItems();
//    System.out.println("Products: #" + products.size());

    MagentoProductConvert converter = new MagentoProductConvert(client);

    for (Product product : products) {
      org.smooth.systems.ec.migration.model.Product prod = converter.convertProduct(product, "it", true);
      String msg = String.format("[%s - %s] %s", prod.getId(), prod.getSku(), prod.getAttributes().get(0).getName());

      System.out.println("\n\nProduct: " + msg);
      System.out.println("ImageUrls: " + prod.getProductImageUrls());
    }
  }
}
