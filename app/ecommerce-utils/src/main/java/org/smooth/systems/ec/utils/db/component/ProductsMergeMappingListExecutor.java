package org.smooth.systems.ec.utils.db.component;

import lombok.extern.slf4j.Slf4j;
import org.smooth.systems.ec.configuration.MigrationConfiguration;
import org.smooth.systems.ec.utils.db.api.IActionExecuter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions> on 03.02.18.
 */
@Slf4j
@Component
public class ProductsMergeMappingListExecutor implements IActionExecuter {

	@Autowired
	private MigrationConfiguration config;

	@Override
	public String getActionName() {
		return "merge-products";
	}

	@Override
	public void execute() {
		log.trace("execute()");
		// read all products for secondary category
		// map product to main product list
	}
}
