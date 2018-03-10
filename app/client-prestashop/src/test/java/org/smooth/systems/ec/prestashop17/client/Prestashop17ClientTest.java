package org.smooth.systems.ec.prestashop17.client;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.smooth.systems.ec.prestashop17.model.Category;
import org.smooth.systems.ec.prestashop17.model.CategoryAttribute;
import org.smooth.systems.ec.prestashop17.model.Language;

public class Prestashop17ClientTest {

  private Prestashop17Client client;

  @Before
  public void setup() {
    client = Prestashop17TestConfiguration.createTestingClient();
  }

  @Test
  public void fetchLanguages() {
    List<Language> languages = client.getLanguages();
    System.out.println("Languages: " + languages);

    List<Category> categories = client.getCategories();
    System.out.println("Categories: " + categories);

    Category cat = client.getCategory(1L);
    System.out.println("Category: " + cat);
  }

  @Test
  public void removeAllCategories() {
    List<CategoryRef> categoriesMetaData = client.getCategoriesMetaData();
    for (CategoryRef categoryRef : categoriesMetaData) {
      if (categoryRef.getId().compareTo(12L) > 0) {
        System.out.println("Remove category: " + categoryRef.getId());
        client.removeCategory(categoryRef.getId());
      }
    }
  }

  @Test
  public void writeSingleCategory() {
    Category category = new Category();
    category.setId(14L);
    category.setActive(1L);
    category.setParentId(12L);

    category.setNames(createTranslatableAttributes("Testt en 1", "Testt de 1", "Testt it 1"));
    category.setDescriptions(createTranslatableAttributes("", "", ""));
    category.setFriendlyUrls(createTranslatableAttributes("test1", "test1", "test1"));

    Category resCategory = client.writeCategory(category);
    System.out.println("Category: " + category);
    System.out.println("Category: " + resCategory);
  }

  @Test
  public void uploadProductImage() {
    Long testProductId = 1L;
    File image1 = new File("src/test/resources/images/test_image_1.jpg");
    File image2 = new File("src/test/resources/images/test_image_2.jpg");
    assertTrue(image1.isFile());
    assertTrue(image2.isFile());

    client.uploadProductImage(testProductId, image1);    
    client.uploadProductImage(testProductId, image2);
  }

  private CategoryAttribute createTranslatableAttributes(String... values) {
    int index = 1;
    CategoryAttribute attr = new CategoryAttribute();
    for (String value : values) {
      attr.addAttribute(new Long(index++), value);
    }
    return attr;
  }
}