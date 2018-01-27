package org.smooth.systems.ec;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.smooth.systems.ec.configuration.MigrationConfiguration;
import org.springframework.boot.ApplicationArguments;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.smooth.systems.utils.ErrorUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractCommandLineRunner {

  protected MigrationConfiguration getMigrationConfiguration(ApplicationArguments args, String configParamName) {
    String configFilePath = retrieveStringParamForName(args, configParamName);
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

  protected Boolean isArgumentSet(ApplicationArguments args, String argsName) {
    return args.containsOption(argsName);
  }
}
