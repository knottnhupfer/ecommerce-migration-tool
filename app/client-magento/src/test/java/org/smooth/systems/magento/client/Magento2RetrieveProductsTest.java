package org.smooth.systems.magento.client;

import java.util.List;

import org.junit.Test;
import org.smooth.systems.ec.magento.mapper.MagentoProductConvert;

import com.github.chen0040.magento.MagentoClient;
import com.github.chen0040.magento.models.Product;
import com.github.chen0040.magento.models.ProductMedia;
import com.github.chen0040.magento.models.ProductPage;

public class Magento2RetrieveProductsTest {

  private static final String MAGENTO_HOSTNAME = "http://data-migration-magento.local/index.php";

  private static final String MAGENTO_USER = "admin";

  private static final String MAGENTO_PASSWD = "secret123";

  @Test
  public void simpleConnection() {
    MagentoClient client = new MagentoClient(MAGENTO_HOSTNAME);
    String token = client.loginAsAdmin(MAGENTO_USER, MAGENTO_PASSWD);
    System.out.println("Token: " + token);

    int pageIndex = 0;
    int pageSize = 10;
    ProductPage page = client.products().page(pageIndex, pageSize);
    List<Product> products = page.getItems();
    System.out.println("Products: " + products.size());

    MagentoProductConvert converter = new MagentoProductConvert();

    for (Product product : products) {
      org.smooth.systems.ec.migration.model.Product prod = converter.convertProduct(product, "it");
      String msg = String.format("[%s] %s", prod.getSku(), prod.getAttributes().get(0).getName());

      System.out.println("Product: " + msg);
    }

    System.out.println("\n\n\n");

    for (Product product : products) {
      // List<String> urls =
      // client.media().getProductMediaAbsoluteUrls(product.getSku());
      System.out.println("URLs for SKU: " + product.getSku());

      // List<String> urls =
      // client.media().getProductMediaRelativeUrls(product.getSku());
      // for (String string : urls) {
      // System.out.println(" - URLs: " + string);
      // }

      List<ProductMedia> mediaList = client.media().getProductMediaList(product.getSku());
      for (ProductMedia productMedia : mediaList) {
        System.out.println(String.format(" - URLs (%s): %s", productMedia.getMedia_type(), productMedia.getFile()));
      }
    }
    // /3/x/3x1_watt_a_4.jpg
  }
}
