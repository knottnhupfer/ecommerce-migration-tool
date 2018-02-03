package org.smooth.systems.ec.utils;

import lombok.extern.slf4j.Slf4j;
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

	public static final String PARAM_ACTION = "action";

	private Map<String, IActionExecuter> actions = new HashMap<>();

	@Autowired
	public EcommerceUtilRunner(List<IActionExecuter> actionsList) {
		for (IActionExecuter action : actionsList) {
			log.info("Action: {}", action.getActionName());
			actions.put(action.getActionName(), action);
		}
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		List<String> actionParams = args.getOptionValues(PARAM_ACTION);
		if(actionParams == null || actionParams.isEmpty()) {
			log.error("No action parameter '{}' defined.", PARAM_ACTION);
			System.exit(5);
		}
		String actionParam = actionParams.get(0);
		log.info("Execute action '{}'", actionParam);
		IActionExecuter action = actions.get(actionParam);
		action.execute();
	}
}
