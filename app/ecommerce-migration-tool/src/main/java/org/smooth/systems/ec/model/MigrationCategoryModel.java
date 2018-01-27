package org.smooth.systems.ec.model;

import java.util.ArrayList;
import java.util.List;

import org.smooth.systems.ec.migration.model.Category;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import lombok.Data;

@Data
@Component
public class MigrationCategoryModel {

  List<Category> readCategories = new ArrayList<>();

  Category mergeCategories;

  List<Category> unmappedCategories = new ArrayList<>();

  List<Category> ignoringCategories = new ArrayList<>();

  List<Category> skippedCategories = new ArrayList<>();

  private final DataModelLogger dataModelLogger;

  public void setMergeCategories(Category category) {
    Assert.notNull(category, "Category is null");
    Assert.notEmpty(category.getAttributes(), "Category attributes is empty");
    Assert.notNull(category.getAttributes().get(0).getName(), "Category attributes name is null");
    mergeCategories = category;
  }

  public void addIgnoringCategory(Category category) {
    Assert.notNull(category, "Category is null");
    Assert.notEmpty(category.getAttributes(), "Category attributes is empty");
    Assert.notNull(category.getAttributes().get(0).getName(), "Category attributes name is null");
    ignoringCategories.add(category);
  }

  public void addUnmappedCategory(Category category) {
    unmappedCategories.add(category);
  }

  public MigrationCategoryModel() {
    dataModelLogger = new DataModelLogger(this);
  }

  public void printDataModel() {
    dataModelLogger.printFullDataModel();
  }

  public void printFullDataModel() {
    dataModelLogger.printFullDataModel();
  }
}
