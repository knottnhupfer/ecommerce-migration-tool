package org.smooth.systems.ec.prestashop17.mapper;

import org.smooth.systems.ec.migration.model.Category;
import org.smooth.systems.ec.migration.model.CategoryTranslateableAttributes;
import org.smooth.systems.ec.prestashop17.component.PrestashopLanguageTranslatorCache;
import org.smooth.systems.ec.prestashop17.model.PrestashopLangAttribute;
import org.smooth.systems.ec.prestashop17.model.LanguageAttribute;

public class CategoryCreationDecorator extends Category {

  private final Category category;

  private final PrestashopLanguageTranslatorCache langCache;

  public CategoryCreationDecorator(PrestashopLanguageTranslatorCache langCache, Long id, Long parentId, boolean display) {
    this.langCache = langCache;

    category = new Category();
    category.setId(id);
    category.setParentId(parentId);
    category.setDisplay(display);
  }

  public void setNames(PrestashopLangAttribute attributes) {
    for (LanguageAttribute attribute : attributes.getValues()) {
      CategoryTranslateableAttributes attr = getTranslatableAttributesForLang(attribute.getId());
      attr.setName(getValueOrEmptyString(attribute.getValue()));
    }
  }

  public void setDescriptions(PrestashopLangAttribute attributes) {
    for (LanguageAttribute attribute : attributes.getValues()) {
      CategoryTranslateableAttributes attr = getTranslatableAttributesForLang(attribute.getId());
      attr.setName(getValueOrEmptyString(attribute.getValue()));
    }
  }

  public void setFriendlyUrls(PrestashopLangAttribute attributes) {
    for (LanguageAttribute attribute : attributes.getValues()) {
      CategoryTranslateableAttributes attr = getTranslatableAttributesForLang(attribute.getId());
      attr.setName(getValueOrEmptyString(attribute.getValue()));
    }
  }

  public Category getCategory() {
    return category;
  }

  private CategoryTranslateableAttributes getTranslatableAttributesForLang(Long id) {
    String code = langCache.getLangCode(id);
    CategoryTranslateableAttributes res = category.getAttributes().stream().filter(attr -> attr.getLangCode().equals(code)).findFirst()
        .orElse(null);
    if (res == null) {
      res = new CategoryTranslateableAttributes(code);
      category.getAttributes().add(res);
    }
    return res;
  }

  private String getValueOrEmptyString(String value) {
    return value != null ? value : "";
  }
}