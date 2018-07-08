package org.smooth.systems.ec.magento19.db.component;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.smooth.systems.ec.client.api.SimpleCategory;
import org.smooth.systems.ec.magento19.db.model.Magento19CategoryText;
import org.smooth.systems.ec.magento19.db.model.Magento19CategoryVarchar;
import org.smooth.systems.ec.magento19.db.repository.CategoryTextRepository;
import org.smooth.systems.ec.magento19.db.repository.CategoryVarcharRepository;
import org.smooth.systems.ec.migration.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertNotNull;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions> on 09.02.18.
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class Magento19DbCategoriesReaderTest {

	@Autowired
	private Magento19DbCategoriesReader categoriesReader;

	@Autowired
	private CategoryTextRepository categoryTextRepo;

	@Autowired
	private CategoryVarcharRepository categoryVarcharRepo;

	@Autowired
	private CategoryPrinter category;

	@Test
	public void simpleRetrieveRootCategories() {
		assertNotNull(categoriesReader);
		Category rootCategory1 = categoriesReader.retrieveCategoriesFromRootCategoryId(2L);
		log.info("Retrieved categories: {}", rootCategory1);

		Category rootCategory2 = categoriesReader.retrieveCategoriesFromRootCategoryId(26L);
		log.info("Retrieved categories: {}", rootCategory2);

		System.out.println("\n\n\nItalian categories");
		category.executeRecursive(rootCategory1);

		System.out.println("\n\n\nGerman categories");
		category.executeRecursive(rootCategory2);
		System.out.println("\n\n\n");
	}

	@Test
	public void retrieveCategoryAttributes() {
		List<Magento19CategoryText> attr1 = categoryTextRepo.findByEntityId(3L);
		List<Magento19CategoryVarchar> attr2 = categoryVarcharRepo.findByEntityId(3L);
		log.info("Magento19CategoryText: {}", attr1);
		log.info("Magento19CategoryVarchar: {}", attr2);
	}

	 @Test
	  public void retrieveCategoryForIdTest() {
	   List<SimpleCategory> categories = Arrays.asList(new SimpleCategory(127L, "it"));
	   List<Category> retrievedCategories = categoriesReader.readAllCategories(categories);
	   log.info("Category: {}", retrievedCategories.get(0).extendedDescription());
	   log.info("");

     categories = Arrays.asList(new SimpleCategory(17L, "it"));
     retrievedCategories = categoriesReader.readAllCategories(categories);
     log.info("Category: {}", retrievedCategories.get(0).extendedDescription());
     log.info("");

     categories = Arrays.asList(new SimpleCategory(11L, "it"));
     retrievedCategories = categoriesReader.readAllCategories(categories);
     log.info("Category: {}", retrievedCategories.get(0).extendedDescription());
     log.info("");
	  }
}