package org.smooth.systems.ec.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.extern.slf4j.Slf4j;
import org.smooth.systems.ec.configuration.MigrationConfiguration;
import org.smooth.systems.utils.ErrorUtil;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
@Configuration
public class ToolConfiguration {

	public static String OPTION_CONFIG_NAME = "config";

	@Bean
	public MigrationConfiguration createConfiguration() {
		return new MigrationConfiguration();
	}

	public MigrationConfiguration getMigrationConfiguration(ApplicationArguments args) {
		String configFilePath = retrieveStringParamForName(args, OPTION_CONFIG_NAME);
		File configFile = new File(configFilePath);
		log.info("Read migration configuration from file: {}", configFile.getAbsolutePath());
		ErrorUtil.throwAndLog(!configFile.isFile(), String.format("File '%s' does not exists", configFile.getAbsolutePath()));
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		try {
			return mapper.readValue(configFile, MigrationConfiguration.class);
		} catch (IOException e) {
			return ErrorUtil.throwAndLog(e.getMessage());
		}
	}

	private String retrieveStringParamForName(ApplicationArguments args, String argsName) {
		List<String> optionValues = args.getOptionValues(argsName);
		ErrorUtil.throwAndLog(optionValues == null || optionValues.size() == 0,
			String.format("Unable to find argument with name: %s", argsName));
		return optionValues.get(0);
	}

//	protected Boolean isArgumentSet(ApplicationArguments args, String argsName) {
//		return args.containsOption(argsName);
//	}
}
