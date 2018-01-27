package org.smooth.systems.ec.prestashop17.mapper;

import java.util.List;

import org.smooth.systems.ec.migration.model.Category;
import org.smooth.systems.ec.migration.model.CategoryTranslateableAttributes;
import org.smooth.systems.ec.prestashop17.component.PrestashopLanguageTranslatorCache;
import org.smooth.systems.ec.prestashop17.model.CategoryAttribute;
import org.springframework.util.Assert;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class CategoryMapper {

  private CategoryMapper() {
  }

  public static org.smooth.systems.ec.prestashop17.model.Category convertCategoryToSystemModel(PrestashopLanguageTranslatorCache langCache,
      Category category, boolean isRootCategory) {
    Assert.isTrue(!category.getAttributes().isEmpty(), String.format("Category(id:%d) has no name", category.getId()));
    log.trace("convertCategoryToSystemModel({})", category.getAttributes().get(0).getName());
    org.smooth.systems.ec.prestashop17.model.Category cat = new org.smooth.systems.ec.prestashop17.model.Category();
    cat.setActive(category.isDisplay() ? 1L : 0L);
    cat.setId(category.getId());
    cat.setParentId(category.getParentId());
    cat.setIsRootCategory(isRootCategory ? 1L : 0L);
    cat.setNames(createNamesCategories(langCache, category));
    cat.setDescriptions(createDescriptionsCategories(langCache, category));
    cat.setFriendlyUrls(createFriendlyUrlsCategories(langCache, category));
    return cat;
  }

  public static Category convertCategoryFromSystemModel(PrestashopLanguageTranslatorCache langCache,
      org.smooth.systems.ec.prestashop17.model.Category category) {
    Assert.isTrue(!category.getNames().getValues().isEmpty(), String.format("Category(id:%d) has no name", category.getId()));
    log.trace("convertCategoryFromSystemModel({})", category.getNames().getValues().get(0));

    CategoryCreationDecorator cat = new CategoryCreationDecorator(langCache, category.getId(), category.getParentId(),
        category.getActive() == 1 ? true : false);
    updateCategoryAttributes(cat, category);
    return cat.getCategory();
    // TODO currently missing attributes
    // cat.setCategoryImageUrl(category.get);
    // cat.setCategoryThumbnailUrl(categoryThumbnailUrl);
    // cat.setSubCategories(subCategories);
  }

  private static void updateCategoryAttributes(CategoryCreationDecorator cat, org.smooth.systems.ec.prestashop17.model.Category category) {
    log.trace("updateCategoryAttributes({}, {})", cat.simpleDescription(), category);
    cat.setNames(category.getNames());
    cat.setDescriptions(category.getDescriptions());
    cat.setFriendlyUrls(category.getFriendlyUrls());
  }

  private static CategoryAttribute createNamesCategories(PrestashopLanguageTranslatorCache langCache, Category category) {
    CategoryAttribute attribute = new CategoryAttribute();
    List<CategoryTranslateableAttributes> attributes = category.getAttributes();
    for (CategoryTranslateableAttributes attr : attributes) {
      attribute.addAttribute(langCache.getLangId(attr.getLangCode()), attr.getName());
    }
    return attribute;
  }

  private static CategoryAttribute createDescriptionsCategories(PrestashopLanguageTranslatorCache langCache, Category category) {
    CategoryAttribute attribute = new CategoryAttribute();
    List<CategoryTranslateableAttributes> attributes = category.getAttributes();
    for (CategoryTranslateableAttributes attr : attributes) {
      attribute.addAttribute(langCache.getLangId(attr.getLangCode()), attr.getDescription());
    }
    return attribute;
  }

  private static CategoryAttribute createFriendlyUrlsCategories(PrestashopLanguageTranslatorCache langCache, Category category) {
    CategoryAttribute attribute = new CategoryAttribute();
    List<CategoryTranslateableAttributes> attributes = category.getAttributes();
    for (CategoryTranslateableAttributes attr : attributes) {
      attribute.addAttribute(langCache.getLangId(attr.getLangCode()), attr.getFriendlyUrl());
    }
    return attribute;
  }
}