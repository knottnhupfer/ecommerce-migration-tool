package org.smooth.systems.ec.category.merging;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.smooth.systems.ec.client.util.ObjectIdMapper;
import org.smooth.systems.ec.configuration.MigrationConfiguration;
import org.smooth.systems.ec.exceptions.NotFoundException;
import org.smooth.systems.ec.migration.model.Category;
import org.smooth.systems.ec.migration.model.CategoryTranslateableAttributes;
import org.smooth.systems.ec.model.MigrationCategoryModel;
import org.smooth.systems.utils.ErrorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DataModelCategoryMerger implements IDataModelCategoryMerger {

  private final MigrationConfiguration config;

  private final MigrationCategoryModel dataModel;

  private ObjectIdMapper idMapper;

  @Autowired
  public DataModelCategoryMerger(MigrationConfiguration config, MigrationCategoryModel dataModel) {
    this.config = config;
    this.dataModel = dataModel;
  }

  @Override
  public void mergeDataModel() {
    log.info("mergeDataModel(rootCategories: {})", dataModel.getReadCategories().size());
    Assert.notEmpty(dataModel.getReadCategories(), "No categories retrieved");

    List<Category> categories = new ArrayList<>(dataModel.getReadCategories());
    dataModel.setMergeCategories(categories.remove(0));
    log.info("Assigned primary category with name: {}", dataModel.getMergeCategories().getAttributes().get(0).getName());
    if (categories.isEmpty()) {
      log.info("No categories to merge");
      return;
    }

		initializeObjectIdMapper();
    for (Category category : categories) {
      mergeCategoryAndSubcategories(category);
    }
  }

  private void mergeCategoryAndSubcategories(Category category) {
    mergeCategory(category);
    for (Category subCategory : category.getSubCategories()) {
      mergeCategoryAndSubcategories(subCategory);
    }
  }

  private void mergeCategory(Category category) {
    Long dstCategoryId;
    try {
      dstCategoryId = idMapper.getMappedIdForId(category.getId());
    } catch (NotFoundException e) {
      dataModel.addUnmappedCategory(category);
      return;
    }

    if (config.getCategoryIdsMergingIgnoreList().contains(category.getId())) {
      dataModel.addIgnoringCategory(category);
      return;
    }

    Category mergeCategory = retrieveMergeCategoryForId(dstCategoryId);
    mergeCategoryIntoCategory(category, mergeCategory);
  }

  private void mergeCategoryIntoCategory(Category category, Category mergeCategory) {
    String catLanguageToMerge = category.getAttributes().get(0).getLangCode();
    Optional<CategoryTranslateableAttributes> attributes = mergeCategory.getAttributes().stream()
        .filter(attr -> catLanguageToMerge.equals(attr.getLangCode())).findFirst();
    ErrorUtil.throwAndLog(attributes.isPresent(), String.format("Attributes for language '%s' already exists in merge category '%s'.",
        catLanguageToMerge, mergeCategory.getAttributes().get(0).getName()));
    mergeCategory.addCategory(category.getAttributes().get(0));
  }

  private Category retrieveMergeCategoryForId(Long categoryId) {
    Category category = dataModel.getMergeCategories();
    return CategoryMappingUtil.retrieveCategoryById(category, categoryId);
  }

  private void initializeObjectIdMapper() {
  	log.debug("initializeObjectIdMapper()");
  	String comment = "# defines the mapping from source category id to created category id in destination system";
		idMapper = new ObjectIdMapper(config.getCategoriesMergingFile(), comment);
	}
}
