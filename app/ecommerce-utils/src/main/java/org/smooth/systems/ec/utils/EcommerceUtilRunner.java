package org.smooth.systems.ec.utils;

import lombok.extern.slf4j.Slf4j;
import org.smooth.systems.ec.common.config.ToolConfiguration;
import org.smooth.systems.ec.configuration.MigrationConfiguration;
import org.smooth.systems.ec.utils.db.api.IActionExecuter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class EcommerceUtilRunner implements ApplicationRunner {

	@Autowired
	protected MigrationConfiguration config;

	@Autowired
	protected ToolConfiguration configReader;

	public static final String PARAM_ACTION = "action";

	private Map<String, IActionExecuter> actions = new HashMap<>();

	@Autowired
	public EcommerceUtilRunner(List<IActionExecuter> actionsList) {
		for (IActionExecuter action : actionsList) {
			log.info("Add action: {}", action.getActionName());
			if(actions.containsKey(action.getActionName())) {
				log.error("Action with name '{}' already exists.", action.getActionName());
				log.error("Registered action: {}, new action: {}", actions.get(action.getActionName()), action);
				throw new RuntimeException(String.format("Action with name '%s' already exists.", action.getActionName()));
			}
			actions.put(action.getActionName(), action);
		}
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		initialize(args);

		List<String> actionParams = args.getOptionValues(PARAM_ACTION);
		if(actionParams == null || actionParams.isEmpty()) {
			log.error("No action parameter '{}' defined.", PARAM_ACTION);
			System.exit(5);
		}
		String actionParam = actionParams.get(0);
		log.info("Execute action '{}'", actionParam);
		IActionExecuter action = actions.get(actionParam);
		action.execute();
		System.exit(0);
	}

	private void initialize(ApplicationArguments args) {
		MigrationConfiguration config = configReader.getMigrationConfiguration(args);
		this.config.storeConfiguration(config);
	}
}
