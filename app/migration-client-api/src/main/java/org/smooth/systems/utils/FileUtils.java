package org.smooth.systems.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Set;

@Slf4j
public class FileUtils {

	public static String CONFIG_PATH = "config/";

	public static String RUNTIME_PATH = "data/";

	public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyyy-mm-dd_hh:mm:ss");

	public static final String getConfigPathName(String fileName) {
		return String.format("%s%s_%s", CONFIG_PATH, DATE_FORMATTER.format(new Date()), fileName);
	}

	public static final Properties readPropertiesFromFile(String fileName) {
		log.info("readPropertiesFromFile({})", fileName);
		File propertiesFile = new File(fileName);
		if (!propertiesFile.getParentFile().isDirectory()) {
			throw new RuntimeException(String.format("No valid data directory '%s'", propertiesFile.getParentFile()));
		}

		Properties properties = new Properties();
		try {
			FileInputStream is = new FileInputStream(propertiesFile);
			properties.load(is);
			is.close();
		} catch (IOException e) {
			ErrorUtil.throwAndLog(
				String.format("Error while reading mapping file '%s'.", propertiesFile.getAbsolutePath(), e));
		}
		return properties;
	}

	public static final void writePropertiesToFile(Properties properties, String fileName, String comment) {
		log.info("writePropertiesToFile({}, {})", fileName, comment);
		File newPropertiesFile = new File(fileName);
		if (!newPropertiesFile.getParentFile().isDirectory()) {
			if (!newPropertiesFile.getParentFile().mkdirs()) {
				throw new RuntimeException(String.format("No valid data directory '%s'", newPropertiesFile.getParentFile()));
			}
		}

		try {
			FileOutputStream out = new FileOutputStream(newPropertiesFile);
			try {
				properties.store(out, comment);
			} catch (FileNotFoundException e) {
				ErrorUtil.throwAndLog(String.format("Unable to open file '%s' for storing properties", fileName), e);
			} catch (IOException e) {
				ErrorUtil.throwAndLog(String.format("Error while writing properties file with comment '%s'", comment), e);
			} finally {
				out.close();
			}
		} catch (IOException e) {
			ErrorUtil.throwAndLog(String.format("Unable to open file '%s' for storing properties", fileName), e);
		}
	}
}
