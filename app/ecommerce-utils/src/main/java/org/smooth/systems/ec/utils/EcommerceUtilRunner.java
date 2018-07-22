package org.smooth.systems.ec.utils;

import lombok.extern.slf4j.Slf4j;
import org.smooth.systems.ec.common.config.ToolConfiguration;
import org.smooth.systems.ec.configuration.MigrationConfiguration;
import org.smooth.systems.ec.utils.db.api.IActionExecuter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class EcommerceUtilRunner implements ApplicationRunner {

	public static final String PARAM_ACTION = "action";

	private Map<String, IActionExecuter> actions = new HashMap<>();

	private MigrationUtilsConfiguration configuration;

	@Autowired
	public EcommerceUtilRunner(List<IActionExecuter> actionsList, MigrationUtilsConfiguration configuration) {
		for (IActionExecuter action : actionsList) {
			log.info("Add action: {}", action.getActionName());
			if(actions.containsKey(action.getActionName())) {
				log.error("Action with name '{}' already exists.", action.getActionName());
				log.error("Registered action: {}, new action: {}", actions.get(action.getActionName()), action);
				throw new RuntimeException(String.format("Action with name '%s' already exists.", action.getActionName()));
			}
			actions.put(action.getActionName(), action);
		}
//		Assert.notNull(configuration.getAction(), "action is null");
		this.configuration = configuration;
	}

	@Override
	public void run(ApplicationArguments args) {
		log.info("run({})", args);

		List<String> actionParams = args.getOptionValues(PARAM_ACTION);
		if((actionParams == null || actionParams.isEmpty()) && !"skip".equalsIgnoreCase(configuration.getAction())) {
		  log.warn("");
			log.error("No action parameter '{}' defined.", PARAM_ACTION);
			log.error("Valid actions are:");
			log.error("  [ACTIONS]  -  {}", actions.keySet());
			log.warn("");
			System.exit(5);
		}
		if("skip".equalsIgnoreCase(configuration.getAction())) {
			log.warn("Skip any action, current action is {}", configuration.getAction());
			return;
		}
		String actionParam = actionParams.get(0);
		log.info("Execute action name '{}'", actionParam);
		IActionExecuter action = actions.get(actionParam);
		log.info("Execute action '{}'", action.getClass().getSimpleName());
		action.execute();
		System.exit(0);
	}
}
