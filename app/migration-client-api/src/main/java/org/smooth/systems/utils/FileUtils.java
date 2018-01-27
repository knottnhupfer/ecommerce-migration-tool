package org.smooth.systems.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtils {

  public static String CONFIG_PATH = "config/";

  public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyyy-mm-dd_hh:mm:ss");

  public static final String getConfigPathName(String fileName) {
    return String.format("%s%s_%s", CONFIG_PATH, DATE_FORMATTER.format(new Date()), fileName);
  }
}
