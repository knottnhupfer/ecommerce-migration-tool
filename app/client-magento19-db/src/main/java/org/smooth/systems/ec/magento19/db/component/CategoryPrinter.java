package org.smooth.systems.ec.magento19.db.component;

import java.util.Collections;

import org.smooth.systems.ec.migration.model.AbstractCategoryRecursiveProcessor;
import org.smooth.systems.ec.migration.model.Category;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

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