package org.smooth.systems.magento.client;

import org.junit.Test;

import com.github.chen0040.magento.MagentoClient;
import com.github.chen0040.magento.models.Category;

public class Magento2ClientTest {

  private static final String MAGENTO_HOSTNAME = "http://data-migration-magento.local/index.php";

  private static final String MAGENTO_USER = "admin";

  private static final String MAGENTO_PASSWD = "secret123";

  @Test
  public void simpleConnection() {
    MagentoClient client = new MagentoClient(MAGENTO_HOSTNAME);
    String token = client.loginAsAdmin(MAGENTO_USER, MAGENTO_PASSWD);
    System.out.println("Token: " + token);

    System.out.println("");
    Category category = client.getCategories().all();
    System.out.println("Category name: " + category.getName());

    category = client.getCategories().getCategoryByIdClean(3);
    System.out.println("Category name: " + category.getName());

    // Category categories = client.getCategories().getProductsInCategory(0);

    // System.out.println("Category name: " + categories.getName());
    // Account myAccount = client.getMyAccount();
    // System.out.println("Account: " + myAccount);
  }
}
