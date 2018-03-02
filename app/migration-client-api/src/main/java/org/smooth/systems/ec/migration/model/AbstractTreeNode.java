package org.smooth.systems.ec.migration.model;

import java.util.List;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions> on 10.02.18.
 */
public interface AbstractTreeNode<T> {

	List<T> getChildrens();
}
