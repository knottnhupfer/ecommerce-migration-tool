package org.smooth.systems.ec.magento19.db.component;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.smooth.systems.ec.magento19.db.model.Magento19Attributes;
import org.smooth.systems.ec.magento19.db.model.Magento19CategoryText;
import org.smooth.systems.ec.magento19.db.model.Magento19CategoryVarchar;
import org.smooth.systems.ec.magento19.db.repository.CategoryTextRepository;
import org.smooth.systems.ec.magento19.db.repository.CategoryVarcharRepository;
import org.smooth.systems.ec.migration.model.AbstractCategoryRecursiveProcessor;
import org.smooth.systems.ec.migration.model.Category;
import org.smooth.systems.ec.migration.model.CategoryTranslateableAttributes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions> on 11.02.18.
 */
@Slf4j
@Component
public class CategoryPrinter extends AbstractCategoryRecursiveProcessor<Category> {

	@Override
	protected void executeTreeNode(Category category, int level) {
		String prefix = String.join("", Collections.nCopies(level - 1, "  "));
		log.info("{} {}", prefix, category.simpleDescription());
	}
}