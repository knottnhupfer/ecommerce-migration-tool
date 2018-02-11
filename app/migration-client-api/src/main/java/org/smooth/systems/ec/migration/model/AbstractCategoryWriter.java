package org.smooth.systems.ec.migration.model;

import org.smooth.systems.ec.client.util.ObjectIdMapper;
import org.smooth.systems.ec.configuration.MigrationConfiguration;
import org.smooth.systems.ec.exceptions.NotFoundException;
import org.smooth.systems.utils.ErrorUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractCategoryWriter extends AbstractCategoryRecursiveProcessor<Category> {

  // TODO retrieve default parent id from application.properties
  private static final Long DEFAULT_ROOT_CATEGORY_ID = 1L;

  private final MigrationConfiguration config;

  private ObjectIdMapper categoryIdMapper;

  protected AbstractCategoryWriter(MigrationConfiguration config) {
    this.config = config;
    categoryIdMapper = new ObjectIdMapper(config.getGeneratedCreatedCategoriesMappingFile());
  }

  @Override
  protected void executeTreeNode(Category category, int level) {
    if (config.getCategoryIdsSkipping().contains(category.getId())) {
      log.error("Skipping category: {}", category.getId());
      return;
    }
    updateRootCategory(category, level);
    Long categoryId = writeCategory(category, level);
    categoryIdMapper.addMapping(category.getId(), categoryId);
    log.info("Created new category with id:{} for source category with id:{}", categoryId, category.getId());
  }

  private void updateRootCategory(Category category, int level) {
    if (level == 1) {
      category.setParentId(DEFAULT_ROOT_CATEGORY_ID);
    } else {
      try {
        category.setParentId(categoryIdMapper.getMappedIdForId(category.getParentId()));
      } catch (NotFoundException e) {
        ErrorUtil.throwAndLog(String.format("Unable to map parent category id:{}. Reason: {}", category.getParentId(), e.getMessage()), e);
      }
    }
  }

  protected abstract Long writeCategory(Category category, int level);

  public ObjectIdMapper getMapper() {
    return categoryIdMapper;
  }
}
