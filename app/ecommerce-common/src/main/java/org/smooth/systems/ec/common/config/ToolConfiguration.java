package org.smooth.systems.ec.common.config;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.smooth.systems.ec.configuration.MigrationConfiguration;
import org.smooth.systems.utils.ErrorUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;

@Slf4j
@Configuration
public class ToolConfiguration {

	@Value("${migration.configuration}")
	private Resource configurationFile;

	@Bean
	public MigrationConfiguration createConfiguration() {
		return getMigrationConfiguration();
	}

  public MigrationConfiguration getMigrationConfiguration() {
    ErrorUtil.throwAndLog(!configurationFile.isFile(), String.format("File '%s' does not exists", configurationFile.getFilename()));
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		try {
			log.info("Read migration configuration from file: {}", configurationFile.getFile().getAbsolutePath());
			return mapper.readValue(configurationFile.getInputStream(), MigrationConfiguration.class);
		} catch (IOException e) {
			return ErrorUtil.throwAndLog(e.getMessage());
		}
	}
}
