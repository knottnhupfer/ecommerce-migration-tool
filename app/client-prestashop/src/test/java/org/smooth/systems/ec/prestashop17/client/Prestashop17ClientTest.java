package org.smooth.systems.ec.prestashop17.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.smooth.systems.ec.exceptions.NotImplementedException;
import org.smooth.systems.ec.prestashop17.model.Category;
import org.smooth.systems.ec.prestashop17.model.ImageUploadResponse.UploadedImage;
import org.smooth.systems.ec.prestashop17.model.Language;
import org.smooth.systems.ec.prestashop17.model.PrestashopLangAttribute;
import org.smooth.systems.ec.prestashop17.model.Tag;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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
    File image2 = new File("src/test/resources/images/test_image_2.jpg");
    assertTrue(image2.isFile());
    UploadedImage uploadedImage = client.uploadProductImage(PrestashopConstantsTests.EXISTING_PRODUCT_ID, image2);
    assertNotNull(uploadedImage);
    assertNotNull(uploadedImage.getId());
    assertEquals(PrestashopConstantsTests.EXISTING_PRODUCT_ID, uploadedImage.getProductId());
  }

  @Test
  public void createProductTest() {

    // Product product = new Product();
    //// product.set
    //
    // File image2 = new File("src/test/resources/images/test_image_2.jpg");
    // assertTrue(image2.isFile());
    // UploadedImage uploadedImage =
    // client.uploadProductImage(PrestashopConstantsTests.EXISTING_PRODUCT_ID,
    // image2);
    // assertNotNull(uploadedImage);
    // assertNotNull(uploadedImage.getId());
    // assertEquals(PrestashopConstantsTests.EXISTING_PRODUCT_ID,
    // uploadedImage.getProductId());
    throw new NotImplementedException();
  }

  @Test
  public void tagsTest() {
    List<Tag> tagsList = client.getTags();
    log.info("Read tags are: {}", tagsList);
    Long newTagId = client.createNewTag(PrestashopConstantsTests.EXISTING_LANG_ID, "tag1");
    log.info("Created tag with id: {}", newTagId);

    List<Tag> updatedTagsList = client.getTags();
    log.info("Updated tags list: {}", updatedTagsList);
    assertEquals(tagsList.size() + 1, updatedTagsList.size());
  }

  private PrestashopLangAttribute createTranslatableAttributes(String... values) {
    int index = 1;
    PrestashopLangAttribute attr = new PrestashopLangAttribute();
    for (String value : values) {
      attr.addAttribute(new Long(index++), value);
    }
    return attr;
  }
}