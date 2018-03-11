package org.smooth.systems.ec.prestashop17.client;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.smooth.systems.ec.exceptions.NotImplementedException;
import org.smooth.systems.ec.prestashop17.model.Category;
import org.smooth.systems.ec.prestashop17.model.ImageUploadResponse;
import org.smooth.systems.ec.prestashop17.model.ImageUploadResponse.UploadedImage;
import org.smooth.systems.ec.prestashop17.model.Language;
import org.smooth.systems.ec.prestashop17.model.Product;
import org.smooth.systems.ec.prestashop17.model.Tag;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class Prestashop17Client {

  public static final String URL_PRODUCT_IMAGE = "/images/products/%d";

  public static final String URL_TAGS = "/tags";
  public static final String URL_TAG = "/tags/%d";

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
    log.info("getLanguages()");
    ResponseEntity<Languages> response = client.getForEntity(baseUrl + "/languages/", Languages.class);
    Languages languages = response.getBody();
    List<Language> res = new ArrayList<>();
    for (ObjectRefId langRef : languages.getWrapper().getLanguages()) {
      ResponseEntity<LanguageWrapper> responseLang = client.getForEntity(baseUrl + "/languages/" + langRef.getId(), LanguageWrapper.class);
      res.add(responseLang.getBody().getLanguage());
    }
    return res;
  }

  public List<Tag> getTags() {
    String tagsUrl = baseUrl + URL_TAGS;
    log.info("getTags({})", tagsUrl);
    ResponseEntity<Tags> response = client.getForEntity(tagsUrl, Tags.class);
    Tags tags = response.getBody();
    List<Tag> res = new ArrayList<>();
    for (ObjectRefId tagRef : tags.getWrapper().getTagReferences()) {
      String tagUrl = baseUrl + String.format(URL_TAG, tagRef.getId());
      ResponseEntity<TagWrapper> responseLang = client.getForEntity(tagUrl, TagWrapper.class);
      res.add(responseLang.getBody().getTag());
    }
    return res;
  }

  public Long createNewTag(Long langId, String tagName) {
    log.debug("createNewTag({}, {})", langId, tagName);
    TagWrapper wrapper = new TagWrapper();
    wrapper.setTag(new Tag(langId, tagName));
    String tagUrl = baseUrl + URL_TAGS;
    ResponseEntity<String> response = client.postForEntity(tagUrl, wrapper, String.class);
    log.info("Created tag: {}", response.getBody());
    return 0L;
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

  public Category writeProduct(Product product) {
    throw new NotImplementedException();
  }

  public UploadedImage uploadProductImage(Long productId, File imageUrl) {
    Assert.notNull(productId, "productId is null");
    Assert.notNull(imageUrl, "imageUrl is null");

    String urlImageUpload = getProductImageUrl(productId);
    log.debug("uploadProductImage({}, {})", urlImageUpload, imageUrl.getAbsolutePath());

    LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
    map.add("image", new FileSystemResource(imageUrl));
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);

    HttpEntity<LinkedMultiValueMap<String, Object>> request = new HttpEntity<LinkedMultiValueMap<String, Object>>(map, headers);
    ResponseEntity<ImageUploadResponse> result = client.exchange(urlImageUpload, HttpMethod.POST, request, ImageUploadResponse.class);
    log.info("Uploaded image: {}", result.getBody());
    return result.getBody().getUploadedImage();
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

  private String getProductImageUrl(Long productId) {
    return String.format(baseUrl + URL_PRODUCT_IMAGE, productId);
  }
}
