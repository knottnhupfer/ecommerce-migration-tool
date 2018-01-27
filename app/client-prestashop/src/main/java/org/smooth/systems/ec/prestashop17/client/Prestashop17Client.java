package org.smooth.systems.ec.prestashop17.client;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.smooth.systems.ec.prestashop17.model.Category;
import org.smooth.systems.ec.prestashop17.model.Language;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class Prestashop17Client {

  private final String baseUrl;

  private final RestTemplate client;

  public Prestashop17Client(String baseUrl, String authToken) {
    Assert.notNull(authToken, "authToken is null");
    this.baseUrl = baseUrl;
    client = new RestTemplate();
    client.getInterceptors().add(new BasicAuthorizationInterceptor(authToken, "invalid"));
    log.info("Initialized client for url: {}", baseUrl);
  }

  public List<Language> getLanguages() {
    ResponseEntity<Languages> response = client.getForEntity(baseUrl + "/languages/", Languages.class);
    Languages languages = response.getBody();
    List<Language> res = new ArrayList<>();
    for (LanguageRef langRef : languages.getWrapper().getLanguages()) {
      ResponseEntity<LanguageWrapper> responseLang = client.getForEntity(baseUrl + "/languages/" + langRef.getId(), LanguageWrapper.class);
      res.add(responseLang.getBody().getLanguage());
    }
    return res;
  }

  public List<CategoryRef> getCategoriesMetaData() {
    log.debug("getCategoriesMetaData()");
    ResponseEntity<Categories> response = client.getForEntity(baseUrl + "/categories/", Categories.class);
    Categories categories = response.getBody();
    log.trace("Retrieved {} category references", categories.getWrapper().getCategoryRefs().size());
    return categories.getWrapper().getCategoryRefs();
  }

  public List<Category> getCategories() {
    log.debug("getCategories()");
    List<CategoryRef> categoryRefs = getCategoriesMetaData();
    log.trace("Retrieved {} categories", categoryRefs.size());

    List<Category> resCategory = new ArrayList<>();
    for (CategoryRef catRef : categoryRefs) {
      Category category = getCategory(catRef.getId());
      resCategory.add(category);
    }
    log.trace("Retrieved {} categories", resCategory.size());
    return resCategory;
  }

  public Category getCategory(Long categoryId) {
    ResponseEntity<CategoryWrapper> response = client.getForEntity(baseUrl + "/categories/" + categoryId, CategoryWrapper.class);
    CategoryWrapper categoryWrapper = response.getBody();
    return categoryWrapper.getCategory();
  }

  public void removeCategory(Long categoryId) {
    client.delete(baseUrl + "/categories/" + categoryId);
  }

  public Category writeCategory(Category category) {
    log.debug("writeCategory({})", category);
    CategoryWrapper catWrapper = new CategoryWrapper();
    catWrapper.setCategory(category);
    catWrapper.getCategory().setId(null);
    printCategory(catWrapper);

    ResponseEntity<CategoryWrapper> response = client.postForEntity(baseUrl + "/categories/", catWrapper, CategoryWrapper.class);
    Category postedCategory = response.getBody().getCategory();
    log.info("Wrote category: {}", postedCategory);
    return postedCategory;
  }

  public void printCategory(CategoryWrapper catWrapper) {
    try {
      JAXBContext jaxbContext = JAXBContext.newInstance(CategoryWrapper.class);
      Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
      jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
      jaxbMarshaller.marshal(catWrapper, System.out);
    } catch (Exception e) {
      log.error("Error while marshalling class: {}", CategoryWrapper.class.getName(), e);
    }
  }
}
