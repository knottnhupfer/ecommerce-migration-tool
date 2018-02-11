package org.smooth.systems.ec.migration.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * Created by Smooth Systems Solutions
 */
@Data
public class CategoryTranslateableAttributes {

  private String langCode;

  private String name;

  private String metaTitle;

	private String shortDescription;

  private String description;

  private String friendlyUrl;

  private List<String> tags = new ArrayList<>();

  public CategoryTranslateableAttributes(String langCode) {
    this.langCode = langCode;
  }
}
