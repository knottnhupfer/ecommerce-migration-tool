package org.smooth.systems.ec.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.Data;
import org.smooth.systems.ec.client.api.SimpleCategory;

import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.List;

@Data
public class MigrationConfiguration {

  @JsonProperty("source-system-name")
  private String sourceSystemName;

  @JsonProperty("destination-system-name")
  private String destinationSystemName;

  @JsonProperty("root-category-id")
  private Long rootCategoryId;

  @JsonProperty("root-category-language")
  private String rootCategoryLanguage;

  @JsonProperty("additional-categories")
  private List<SimpleCategory> additionalCategories;

  @JsonProperty("categories-merging-file")
  private String categoriesMergingFile;

  @JsonProperty("category-ids-merging-ignore-list")
  private List<Long> categoryIdsMergingIgnoreList;

  @JsonProperty("category-ids-skipping")
  private List<Long> categoryIdsSkipping;

  @JsonProperty("generated-created-categories-mapping-file")
  private String generatedCreatedCategoriesMappingFile;

  @JsonProperty("products-brand-mapping")
  private String productsBrandMappingFile;

  @JsonProperty("products-merging-file")
  private String productsMergingFile;

  // @JsonProperty("generated-active-products-file")
  // private String generatedActiveProductsFile;

  @JsonProperty("generated-products-merging-file")
  private String generatedProductsMergingFile;

  @JsonProperty("product-ids-skipping")
  private List<Long> productIdsSkipping;

  @JsonProperty("generated-products-migration-file")
  private String generatedProductsMigrationFile;

  @JsonProperty("products-images-directory")
  private String productsImagesDirectory;

  @Override
  public String toString() {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    try {
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      mapper.writeValue(os, this);
      return new String(os.toByteArray(), "UTF-8");
    } catch (Exception e) {
      return "ERROR: Unable to convert object to string.";
    }
  }

  public void storeConfiguration(MigrationConfiguration config) {
    sourceSystemName = config.sourceSystemName;
    destinationSystemName = config.destinationSystemName;
    rootCategoryId = config.rootCategoryId;
    rootCategoryLanguage = config.rootCategoryLanguage;
    additionalCategories = config.additionalCategories;

    categoriesMergingFile = config.categoriesMergingFile;
    generatedCreatedCategoriesMappingFile = config.generatedCreatedCategoriesMappingFile;
    categoryIdsMergingIgnoreList = config.categoryIdsMergingIgnoreList != null ? config.categoryIdsMergingIgnoreList
            : Collections.emptyList();
    categoryIdsSkipping = config.categoryIdsSkipping != null ? config.categoryIdsSkipping : Collections.emptyList();

    productsImagesDirectory = config.productsImagesDirectory;
    productsMergingFile = config.productsMergingFile;
    generatedProductsMergingFile = config.generatedProductsMergingFile;
    productIdsSkipping = config.productIdsSkipping != null ? config.productIdsSkipping : Collections.emptyList();

    productsBrandMappingFile = config.productsBrandMappingFile;
    generatedProductsMigrationFile = config.generatedProductsMigrationFile;
  }
}