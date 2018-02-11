package org.smooth.systems.ec.migration.model;

import java.util.List;

public abstract class AbstractCategoryRecursiveProcessor<T extends AbstractTreeNode> {

	public void executeRecursive(T rootCategory) {
		executeCategories(rootCategory, 1);
	}

	private void executeCategories(T category, int level) {
		executeTreeNode(category, level);
		List<T> childrens = category.getChildrens();
		for (T subCategory : childrens) {
			executeCategories(subCategory, level + 1);
		}
	}

	protected abstract void executeTreeNode(T category, int level);
}