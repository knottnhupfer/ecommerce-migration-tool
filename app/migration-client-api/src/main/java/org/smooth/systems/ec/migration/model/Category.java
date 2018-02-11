package org.smooth.systems.ec.migration.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * Created by Smooth Systems Solutions
 */
@Data
public class Category implements AbstractTreeNode<Category> {

  private Long id;

  private Long parentId;

  private boolean display;

  private String categoryImageUrl;

  private String categoryThumbnailUrl;

  // id of the new created category at the destination system
  private Long createdCategoryId;

  private List<CategoryTranslateableAttributes> attributes = new ArrayList<>();

  private List<Category> childrens = new ArrayList<>();

  public void addCategory(CategoryTranslateableAttributes attribute) {
    attributes.add(attribute);
  }

  public void addSubCategory(Category subCategory) {
		childrens.add(subCategory);
  }

  public String simpleDescription() {
    return String.format("Category(id=%d, name=%s, subCategories=%d)", id, attributes.get(0).getName(),
			childrens == null ? 0 : childrens.size());
  }

  public String extendedDescription() {
    StringBuilder builder = new StringBuilder();
    builder.append(String.format("Category[%d] with %d subCategories", id, childrens == null ? 0 : childrens.size()));
    for (CategoryTranslateableAttributes att : attributes) {
      builder.append(String.format("             language=%s, name=%s", att.getLangCode(), att.getName()));
    }
    return builder.toString();
  }
}
