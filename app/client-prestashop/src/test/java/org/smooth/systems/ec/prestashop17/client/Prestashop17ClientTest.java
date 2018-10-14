package org.smooth.systems.ec.prestashop17.client;

import static java.lang.Thread.sleep;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.Before;
import org.junit.Test;
import org.smooth.systems.ec.prestashop17.component.PrestashopLanguageTranslatorCache;
import org.smooth.systems.ec.prestashop17.model.*;
import org.smooth.systems.ec.prestashop17.model.ImageUploadResponse.UploadedImage;
import org.smooth.systems.ec.prestashop17.model.Product.Visibility;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Prestashop17ClientTest {

  public static final Long EXISTING_CATEGORY_ID = 1L;

  private Prestashop17Client client;

  private PrestashopLanguageTranslatorCache langCache;

  @Before
  public void setup() {
    client = Prestashop17TestConfiguration.createTestingClient();
  }

  @Test
  public void fetchBasicTypes() {
    List<Language> languages = client.getLanguages();
    System.out.println("Languages: " + languages);
    assertEquals(3, languages.size());

//    List<Category> categories = client.getCategories();
//    System.out.println("Categories: " + categories);
//
//    Category cat = client.getCategory(1L);
//    System.out.println("Category: " + cat);
  }

  @Test
  public void fetchLanguages() {
    List<Language> languages = client.getLanguages();
    System.out.println("Languages: " + languages);
    assertEquals(3, languages.size());

    List<Language> lang = languages.stream().filter(elem -> "en".equals(elem.getIsoCode())).collect(Collectors.toList());
    assertEquals(1, lang.size());
    lang = languages.stream().filter(elem -> "de".equals(elem.getIsoCode())).collect(Collectors.toList());
    assertEquals(1, lang.size());
    lang = languages.stream().filter(elem -> "it".equals(elem.getIsoCode())).collect(Collectors.toList());
    assertEquals(1, lang.size());
  }

  @Test
  public void removeAllCategories() {
    List<ObjectRefId> categoriesMetaData = client.getCategoriesMetaData();
    for (ObjectRefId categoryRef : categoriesMetaData) {
      if (categoryRef.getId().compareTo(12L) > 0) {
        System.out.println("Remove category: " + categoryRef.getId());
//        client.removeCategory(categoryRef.getId());
      }
    }
  }

  @Test
  public void writeSingleCategory() {
    initializeLanguageCache();
    Category category = new Category();
    category.setId(14L);
    category.setActive(1L);
    category.setIsRootCategory(1L);
    category.setParentId(EXISTING_CATEGORY_ID);

    category.setNames(createTranslatableAttributes("LED Beleuchtung für Geschäfte", "Testt de 1", "Testt it 1"));
    category.setDescriptions(createTranslatableAttributes("", "", ""));
    category.setFriendlyUrls(createTranslatableAttributes("test1", "test1", "test1"));

    client.writeCategory(langCache, category);
  }

  @Test
  public void uploadProductImageTest() {
    File image2 = new File("src/test/resources/images/test_image_2.jpg");
    assertTrue(image2.isFile());
    UploadedImage uploadedImage = client.uploadProductImage(PrestashopConstantsTests.EXISTING_PRODUCT_ID, image2);
    assertNotNull(uploadedImage);
    assertNotNull(uploadedImage.getId());
    assertEquals(PrestashopConstantsTests.EXISTING_PRODUCT_ID, uploadedImage.getProductId());
  }

	@Test
	public void readCompleteProductTest() {
		CompleteProduct completeProduct = client.getCompleteProduct(533L);
		log.info("Fetched product: {}", completeProduct);

		completeProduct = client.getCompleteProduct(404L);
		log.info("Fetched product: {}", completeProduct);
	}

  @Test
  public void createProductTest() {
    Product product = new Product();
    String random_string = UUID.randomUUID().toString().substring(16);

    product.setNetPrice(23.6);
    product.setWeight("7.5");
    product.setReference("sku_created_" + random_string);
    product.setVisibility(Visibility.both);
    product.setManufacturerId(PrestashopConstantsTests.EXISTING_BRAND_ID);

    // product.addTagId(PrestashopConstantsTests.EXISTING_TAG_ID_1);
    // product.addTagId(PrestashopConstantsTests.EXISTING_TAG_ID_2);
    product.addCategoryId(PrestashopConstantsTests.EXISTING_CATEGORY_ID);

    product.setNames(createTranslatableAttributesList("English product", "Deutsch Produkt", "Prodotto italiano"));
    product.setDescriptions(createTranslatableAttributesList("description", "Beschreibung", "descrizione"));
		product.setFriendlyUrls(createTranslatableAttributesList(random_string + "-en", random_string + "-de", random_string + "-it"));
    product.setShortDescriptions(createTranslatableAttributesList("short description", "Beschreibung kurz", "descrizione corta"));

    product.setTaxRuleGroup(5L);
    client.writeProduct(product);
    // Product createdProduct = client.writeProduct(product);
    // log.info("Product: {}", createdProduct);
    // log.info("ProductAssociations: {}",
    // createdProduct.getAssociations());
  }

  @Test
  public void removeProductsTest() {
    Long[] productIds = { 10002L, 10003L, 10004L, 10005L, 10006L, 10007L, 10008L, 10009L, 10010L };
    // Long[] productIds = {7L, 8L, 9L, 10L, 11L, 12L};
    for (Long productId : productIds) {
      client.deleteProduct(productId);
    }
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

	@Test
	public void fetchAndUploadTest() {
  	Long productId = 970L;
  	String path = "src/test/resources/expected_result/";
		CompleteProduct product = readCompleteProductAndWriteToFile(productId, path + "retrieved_result_beginning.xml");

		log.info("Product:\n{}", product);
		product.setWeight("2500.000000");
		client.updateProduct(product);
		readCompleteProductAndWriteToFile(productId, path + "retrieved_result_new_weight.xml");

		product.setWeight("1500.000000");
		client.updateProduct(product);
		readCompleteProductAndWriteToFile(productId, path + "retrieved_result_reverted_back.xml");
	}

	@Test
	public void fetchDescriptionsAndDeserializeTest() {
		Long productId = 968L;
		ProductDescriptions product = client.getProductDescriptions(productId);
		log.info("Product:\n{}", product);
	}

  @Test
  public void enableIgnoreOutOfStockTest() {
    client.enableIgnoreStock(38L);
  }

	private LanguageContainer createTranslatableAttributesList(String... values) {
		long index = 1;
		List<LanguageAttribute> attrs = new ArrayList<>();
		for(String value : values) {
			attrs.add(new LanguageAttribute(index++, value));
		}
		return new LanguageContainer(attrs);
	}

  private PrestashopLangAttribute createTranslatableAttributes(String... values) {
    long index = 1;
    PrestashopLangAttribute attr = new PrestashopLangAttribute();
    for (String value : values) {
      attr.addAttribute(new Long(index++), value);
    }
    return attr;
  }

  private void initializeLanguageCache() {
    if (langCache == null) {
      PrestashopLanguageTranslatorCache prestashopLanguageTranslatorCache = new PrestashopLanguageTranslatorCache(client);
      prestashopLanguageTranslatorCache.initialize();
      langCache = prestashopLanguageTranslatorCache;
    }
  }

  private void writeToFile(String data, String fileName) {
  	try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
			writer.write(data);
			writer.close();
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
  // public static PrestashopLangAttribute createAttributes(String...
  // attributes) {
  // assertTrue(attributes.length >= 0 && attributes.length <= 3);
  // Long langIndex = 0L;
  // PrestashopLangAttribute result = new PrestashopLangAttribute();
  // for(String attr : attributes) {
  // result.addAttribute(langIndex++, attr);
  // }
  // return result;
  // }

	private CompleteProduct readCompleteProductAndWriteToFile(Long productId, String filePath) {
		CompleteProduct product = client.getCompleteProduct(productId);
		try {
			XmlMapper xmlMapper = new XmlMapper();
			String productAsString = xmlMapper.writeValueAsString(product);
			writeToFile(productAsString, filePath);
			return product;
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void sleep(long milliseconds) {
  	try {
			Thread.sleep(milliseconds);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}